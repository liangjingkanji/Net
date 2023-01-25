package com.drake.net.interceptor

import com.drake.net.body.name
import com.drake.net.body.peekBytes
import com.drake.net.body.value
import com.drake.net.log.LogRecorder
import okhttp3.*

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
open class LogRecordInterceptor(
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
            generateId, request.url.toString(), request.method, request.headers.toMultimap(), requestString(request)
        )
        try {
            val response = chain.proceed(request)
            LogRecorder.recordResponse(
                generateId, System.currentTimeMillis(), response.code, response.headers.toMultimap(), responseString(response)
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
    protected open fun requestString(request: Request): String? {
        val body = request.body ?: return null
        val mediaType = body.contentType()
        return when {
            body is MultipartBody -> body.parts.joinToString("&") {
                "${it.name()}=${it.value()}"
            }
            body is FormBody -> body.peekBytes(requestByteCount).utf8()
            arrayOf("plain", "json", "xml", "html").contains(mediaType?.subtype) -> body.peekBytes(requestByteCount).utf8()
            else -> "$mediaType does not support output logs"
        }
    }

    /**
     * 响应字符串
     */
    protected open fun responseString(response: Response): String? {
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