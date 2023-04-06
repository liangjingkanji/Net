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
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Net代理OkHttp的拦截器
 */
object NetOkHttpInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val reqBody = request.body?.toNetRequestBody(request.uploadListeners())
        val cache = request.tagOf<ForceCache>() ?: NetConfig.forceCache
        val cacheMode = request.tagOf<CacheMode>()
        request = request.newBuilder().apply {
            if (cache != null && cacheMode != null) {
                cacheControl(CacheControl.Builder().noCache().noStore().build())
            }
        }.method(request.method, reqBody).build()

        try {
            attach(chain)
            val response = if (cache != null) {
                when (cacheMode) {
                    CacheMode.READ -> cache.get(request) ?: throw NoCacheException(request)
                    CacheMode.READ_THEN_REQUEST -> cache.get(request) ?: chain.proceed(request).run {
                        cache.put(this)
                    }
                    CacheMode.REQUEST_THEN_READ -> try {
                        chain.proceed(request).run {
                            cache.put(this)
                        }
                    } catch (e: Exception) {
                        cache.get(request) ?: throw NoCacheException(request)
                    }
                    CacheMode.WRITE -> chain.proceed(request).run {
                        cache.put(this)
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
            throw NetUnknownHostException(request, message = e.message)
        } catch (e: NetException) {
            throw e
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
}