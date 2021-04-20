package com.drake.net.interceptor

import com.drake.net.request.BaseRequest

interface RequestInterceptor {

    /**
     * 当你使用Net发起请求的时候就会触发该拦截器
     * 该拦截器属于轻量级不具备重发的功能, 一般用于请求参数的修改
     * 请勿在这里进行请求重发可能会导致死循环
     */
    fun interceptor(request: BaseRequest)
}