package com.drake.net.interceptor

import androidx.annotation.IntRange
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.closeQuietly

/**
 * 重试次数拦截器
 *
 * @property retryCount 重试次数
 */
class RetryInterceptor(@IntRange(from = 1) val retryCount: Int = 3) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var retryCount = 0
        val request = chain.request()
        var response = chain.proceed(request)
        while (!response.isSuccessful && retryCount < this.retryCount) {
            retryCount++
            response.closeQuietly()
            response = chain.proceed(request)
        }
        return response
    }
}