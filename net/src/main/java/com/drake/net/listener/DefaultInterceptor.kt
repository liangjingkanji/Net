/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.listener

import com.drake.net.NetConfig
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.connect.Interceptor
import com.yanzhenjie.kalle.connect.http.Chain
import java.io.IOException

/**
 * 响应体的日志打印器
 */
class DefaultInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        NetConfig.listener?.net(request, response)
        return response
    }
}

