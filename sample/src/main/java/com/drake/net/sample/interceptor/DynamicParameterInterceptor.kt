package com.drake.net.sample.interceptor

import com.yanzhenjie.kalle.RequestMethod
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.connect.Interceptor
import com.yanzhenjie.kalle.connect.http.Chain
import com.yanzhenjie.kalle.simple.SimpleBodyRequest
import com.yanzhenjie.kalle.simple.SimpleUrlRequest

class DynamicParameterInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        var request = chain.request()

        // 除非是Download*函数否则都仅有两种Request: SimpleBodyRequest/SimpleUrlRequest
        request = when (request.method()) {
            RequestMethod.POST -> {
                val copyParams =
                    request.copyParams().builder().add("token", "dsgfahfty1231").build() // 添加一个参数
                SimpleBodyRequest.newBuilder(request.url(), request.method())
                    .setParams(copyParams)
                    .setHeaders(request.headers())
                    .tag(request.tag())
                    .build()
            }
            RequestMethod.GET -> {
                val copyParams =
                    request.copyParams().builder().add("ip", "127.0.0.1").build()
                SimpleUrlRequest.newBuilder(request.url(), request.method())
                    .setParams(copyParams)
                    .setHeaders(request.headers())
                    .tag(request.tag())
                    .build()
            }
            else -> request
        }
        return chain.proceed(request)
    }
}