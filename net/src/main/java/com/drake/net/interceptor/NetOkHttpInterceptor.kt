/*
 * MIT License
 *
 * Copyright (c) 2023 劉強東 https://github.com/liangjingkanji
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.drake.net.interceptor

import com.drake.net.NetConfig
import com.drake.net.body.toNetRequestBody
import com.drake.net.body.toNetResponseBody
import com.drake.net.cache.CacheMode
import com.drake.net.cache.ForceCache
import com.drake.net.exception.*
import com.drake.net.request.downloadListeners
import com.drake.net.request.downloadPartialStartRange
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
        val reqBody = request.body?.toNetRequestBody(
            request.uploadListeners()
        )
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
                    CacheMode.READ_THEN_REQUEST -> cache.get(request) ?: chain.proceed(request)
                        .run {
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
            val respBody = response.body?.toNetResponseBody(
                request.downloadPartialStartRange(),
                request.downloadListeners()
            ) {
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