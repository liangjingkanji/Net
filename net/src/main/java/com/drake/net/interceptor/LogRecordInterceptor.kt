package com.drake.net.interceptor

import android.util.Log
import com.drake.net.log.LogRecorder
import com.drake.net.request.label
import com.drake.net.request.logRecord
import com.drake.net.request.logString
import com.drake.net.response.logString
import com.drake.net.tag.NetLabel
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 网络日志记录器
 * 可以参考此拦截器为项目中其他网络请求库配置. 本拦截器属于标准的OkHttp拦截器适用于所有OkHttp拦截器内核的网络请求库
 * 如果项目中的日志需要特殊情况, 例如请求体加密, 响应体解密, 请继承本拦截器进行自定义
 * 使用Request/Response的peekString函数可以复制请求和响应字符串
 *
 * 在正式环境下请禁用此日志记录器. 因为他会消耗少量网络速度
 *
 * @property enabled 是否启用日志输出
 * @property requestByteCount 请求日志输出字节数, -1 则为全部
 * @property responseByteCount 响应日志输出字节数, -1 则为全部
 */
open class LogRecordInterceptor(
    val enabled: Boolean,
    val requestByteCount: Long = 1024 * 1024,
    val responseByteCount: Long = 1024 * 1024 * 4
) : Interceptor {

    init {
        LogRecorder.enabled = enabled
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.label<NetLabel.LogRecord>()

        if (request.logRecord == false) {
            return chain.proceed(request)
        }

        val generateId = LogRecorder.generateId()
        LogRecorder.recordRequest(
            generateId,
            request.url.toString(),
            request.method,
            request.headers.toMultimap(),
            requestString(request)
        )
        try {
            val response = chain.proceed(request)
            LogRecorder.recordResponse(
                generateId,
                System.currentTimeMillis(),
                response.code,
                response.headers.toMultimap(),
                responseString(response)
            )
            return response
        } catch (e: Exception) {
            LogRecorder.recordException(
                generateId,
                System.currentTimeMillis(),
                -1,
                null,
                Log.getStackTraceString(e)
            )
            throw e
        }
    }

    /**
     * 请求字符串
     */
    protected open fun requestString(request: Request): String? {
        return request.logString(requestByteCount)
    }

    /**
     * 响应字符串
     */
    protected open fun responseString(response: Response): String? {
        return response.logString(responseByteCount)
    }
}