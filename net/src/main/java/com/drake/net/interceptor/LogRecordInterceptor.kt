package com.drake.net.interceptor

import android.util.Log
import com.drake.net.log.LogRecorder
import com.drake.net.request.isLogRecord
import com.drake.net.request.label
import com.drake.net.request.logString
import com.drake.net.response.logString
import com.drake.net.tag.NetLabel
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 网络日志记录器
 * 可以参考此拦截器为项目中其他网络请求库配置. 本拦截器属于标准的OkHttp拦截器适用于所有OkHttp拦截器内核的网络请求库
 *
 * 在正式环境下请禁用此日志记录器. 因为他会消耗少量网络速度
 *
 * @property enabled 是否启用日志输出
 * @property requestByteCount 请求日志输出字节数
 * @property responseByteCount 响应日志输出字节数
 */
class LogRecordInterceptor(
    val enabled: Boolean,
    val requestByteCount: Long = 1024 * 1024,
    val responseByteCount: Long = 1024 * 1024 * 4
) : Interceptor {

    init {
        LogRecorder.enabled = enabled
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.label<NetLabel.RecordLog>()

        if (request.isLogRecord() == false) {
            return chain.proceed(request)
        }

        val generateId = LogRecorder.generateId()
        LogRecorder.recordRequest(
            generateId,
            request.url.toString(),
            request.method,
            request.headers.toMultimap(),
            request.logString(requestByteCount)
        )
        try {
            val response = chain.proceed(request)
            LogRecorder.recordResponse(
                generateId,
                System.currentTimeMillis(),
                response.code,
                response.headers.toMultimap(),
                response.logString(responseByteCount)
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
}