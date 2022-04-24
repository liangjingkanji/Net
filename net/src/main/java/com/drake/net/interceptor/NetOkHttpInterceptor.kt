package com.drake.net.interceptor

import com.drake.net.NetConfig
import com.drake.net.body.toNetRequestBody
import com.drake.net.body.toNetResponseBody
import com.drake.net.cache.CacheMode
import com.drake.net.cache.ForceCache
import com.drake.net.exception.*
import com.drake.net.request.downloadListeners
import com.drake.net.request.tagOf
import com.drake.net.request.uploadListeners
import com.drake.net.utils.isNetworking
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.discard
import okhttp3.internal.http.ExchangeCodec
import okhttp3.internal.http.RealResponseBody
import okio.Buffer
import okio.Source
import okio.buffer
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * Net代理OkHttp的拦截器
 */
object NetOkHttpInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val reqBody = request.body?.toNetRequestBody(request.uploadListeners())
        val cache = request.tagOf<ForceCache>() ?: NetConfig.forceCache
        request = request.newBuilder().apply {
            if (cache != null) cacheControl(CacheControl.Builder().noCache().noStore().build())
        }.method(request.method, reqBody).build()

        try {
            attach(chain)
            val response = if (cache != null) {
                when (request.tagOf<CacheMode>()) {
                    CacheMode.READ -> cache.get(request) ?: throw NoCacheException(request)
                    CacheMode.READ_THEN_REQUEST -> cache.get(request) ?: chain.proceed(request).run {
                        cacheWritingResponse(cache, this)
                    }
                    CacheMode.REQUEST_THEN_READ -> try {
                        chain.proceed(request).run {
                            cacheWritingResponse(cache, this)
                        }
                    } catch (e: Exception) {
                        cache.get(request) ?: throw NoCacheException(request)
                    }
                    CacheMode.WRITE -> chain.proceed(request).run {
                        cacheWritingResponse(cache, this)
                    }
                    else -> chain.proceed(request)
                }
            } else {
                chain.proceed(request)
            }
            val respBody = response.body?.toNetResponseBody(request.downloadListeners()) {
                detach(chain.call())
            }
            return response.newBuilder().body(respBody).build()
        } catch (e: SocketTimeoutException) {
            throw NetSocketTimeoutException(request, e.message, e)
        } catch (e: ConnectException) {
            throw NetConnectException(request, cause = e)
        } catch (e: UnknownHostException) {
            val isNetworking = try {
                NetConfig.app.isNetworking()
            } catch (e: Exception) {
                true
            }
            if (isNetworking) {
                throw NetUnknownHostException(request, message = e.message)
            } else {
                throw NetworkingException(request)
            }
        } catch (e: Throwable) {
            throw HttpFailureException(request, cause = e)
        }
    }

    private fun attach(chain: Interceptor.Chain) {
        NetConfig.runningCalls.add(WeakReference(chain.call()))
    }

    private fun detach(call: Call) {
        val iterator = NetConfig.runningCalls.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().get() == call) {
                iterator.remove()
                return
            }
        }
    }

    /** 缓存网络响应 */
    @Throws(IOException::class)
    private fun cacheWritingResponse(cache: ForceCache, response: Response): Response {
        // Some apps return a null body; for compatibility we treat that like a null cache request.
        if (!response.isSuccessful) return response
        val cacheRequest = cache.put(response) ?: return response
        val cacheBodyUnbuffered = cacheRequest.body()

        val source = response.body!!.source()
        val cacheBody = cacheBodyUnbuffered.buffer()

        val cacheWritingSource = object : Source {
            private var cacheRequestClosed = false

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead: Long
                try {
                    bytesRead = source.read(sink, byteCount)
                } catch (e: IOException) {
                    if (!cacheRequestClosed) {
                        cacheRequestClosed = true
                        cacheRequest.abort() // Failed to write a complete cache response.
                    }
                    throw e
                }

                if (bytesRead == -1L) {
                    if (!cacheRequestClosed) {
                        cacheRequestClosed = true
                        cacheBody.close() // The cache response is complete!
                    }
                    return -1
                }

                sink.copyTo(cacheBody.buffer, sink.size - bytesRead, bytesRead)
                cacheBody.emitCompleteSegments()
                return bytesRead
            }

            override fun timeout() = source.timeout()

            @Throws(IOException::class)
            override fun close() {
                if (!cacheRequestClosed &&
                    !discard(ExchangeCodec.DISCARD_STREAM_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
                    cacheRequestClosed = true
                    cacheRequest.abort()
                }
                source.close()
            }
        }

        val contentType = response.header("Content-Type")
        val contentLength = response.body?.contentLength() ?: 0
        return response.newBuilder()
            .body(RealResponseBody(contentType, contentLength, cacheWritingSource.buffer()))
            .build()
    }
}