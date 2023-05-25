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

package com.drake.net.okhttp

import com.drake.net.interceptor.NetOkHttpInterceptor
import com.drake.net.request.tagOf
import com.drake.net.tag.NetTag
import okhttp3.OkHttpClient

/**
 * Net要求经过该函数处理创建特殊的OkHttpClient
 */
fun OkHttpClient.toNetOkhttp() = run {
    if (!interceptors.contains(NetOkHttpInterceptor)) {
        newBuilder().addInterceptor(NetOkHttpInterceptor).build()
    } else {
        this
    }
}

/**
 * 取消OkHttp客户端中指定Id的请求
 * 如果使用的是Net创建的网络请求请使用[com.drake.net.Net.cancelId]
 */
fun OkHttpClient.cancelId(id: Any?) {
    id ?: return
    dispatcher.runningCalls().forEach {
        if (id === it.request().tagOf<NetTag.RequestId>()?.value) {
            it.cancel()
        }
    }
    dispatcher.queuedCalls().forEach {
        if (id === it.request().tagOf<NetTag.RequestId>()?.value) {
            it.cancel()
        }
    }
}

/**
 * 取消OkHttp客户端中指定Group的请求
 * 如果使用的是Net创建的网络请求请使用[com.drake.net.Net.cancelGroup]
 */
fun OkHttpClient.cancelGroup(group: Any?) {
    group ?: return
    dispatcher.runningCalls().forEach {
        if (group === it.request().tagOf<NetTag.RequestGroup>()?.value) {
            it.cancel()
        }
    }
    dispatcher.queuedCalls().forEach {
        if (group === it.request().tagOf<NetTag.RequestGroup>()?.value) {
            it.cancel()
        }
    }
}
