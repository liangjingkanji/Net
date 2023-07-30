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

import com.drake.net.body.name
import com.drake.net.body.peekBytes
import com.drake.net.body.value
import com.drake.net.log.LogRecorder
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.Response

/**
 * 网络日志记录器
 * 可以参考此拦截器为项目中其他网络请求库配置. 本拦截器属于标准的OkHttp拦截器适用于所有OkHttp拦截器内核的网络请求库
 *
 * 在正式环境下请禁用此日志记录器. 因为他会消耗少量网络速度
 *
 * @property enabled 是否启用日志输出
 * @property requestByteCount 请求日志输出字节数, -1 则为全部
 * @property responseByteCount 响应日志输出字节数, -1 则为全部
 */
open class LogRecordInterceptor @JvmOverloads constructor(
    var enabled: Boolean,
    var requestByteCount: Long = 1024 * 1024,
    var responseByteCount: Long = 1024 * 1024 * 4
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!enabled) {
            return chain.proceed(request)
        }

        val generateId = LogRecorder.generateId()
        LogRecorder.recordRequest(
            generateId, request.url.toString(), request.method, request.headers.toMultimap(), getRequestLog(request)
        )
        try {
            val response = chain.proceed(request)
            LogRecorder.recordResponse(
                generateId, System.currentTimeMillis(), response.code, response.headers.toMultimap(), getResponseLog(response)
            )
            return response
        } catch (e: Exception) {
            val error = "Review LogCat for details, occurred exception: ${e.javaClass.simpleName}"
            LogRecorder.recordException(generateId, System.currentTimeMillis(), -1, null, error)
            throw e
        }
    }

    /**
     * 请求字符串
     */
    protected open fun getRequestLog(request: Request): String? {
        val body = request.body ?: return null
        val mediaType = body.contentType()
        return when {
            body is MultipartBody -> {
                body.parts.joinToString("&") {
                    "${it.name()}=${it.value()}"
                }
            }

            body is FormBody -> {
                body.peekBytes(requestByteCount).utf8()
            }

            arrayOf("plain", "json", "xml", "html").contains(mediaType?.subtype) -> {
                body.peekBytes(requestByteCount).utf8()
            }

            else -> {
                "$mediaType does not support output logs"
            }
        }
    }

    /**
     * 响应字符串
     */
    protected open fun getResponseLog(response: Response): String? {
        val body = response.body ?: return null
        val mediaType = body.contentType()
        val isPrintType = arrayOf("plain", "json", "xml", "html").contains(mediaType?.subtype)
        return if (isPrintType) {
            body.peekBytes(responseByteCount).utf8()
        } else {
            "$mediaType does not support output logs"
        }
    }
}