package com.drake.net.sample.interfaces

import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.request.BaseRequest


/** 请求拦截器, 一般用于添加全局参数 */
class MyRequestInterceptor : RequestInterceptor {

    /** 本方法每次请求发起都会调用, 这里添加的参数可以是动态参数 */
    override fun interceptor(request: BaseRequest) {
        // 仅请求动作没有添加时才会添加默认请求头
        if (request.headers()["client"] == null) {
            request.addHeader("client", "Android")
        }
        request.setHeader("token", "6cad0ff06f5a214b9cfdf2a4a7c43339")
    }
}