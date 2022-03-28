/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.okhttp

import com.drake.net.interceptor.NetOkHttpInterceptor
import com.drake.net.request.label
import com.drake.net.tag.NetLabel
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
        if (id === it.request().label<NetLabel.RequestId>()?.value) {
            it.cancel()
        }
    }
    dispatcher.queuedCalls().forEach {
        if (id === it.request().label<NetLabel.RequestId>()?.value) {
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
        if (group === it.request().label<NetLabel.RequestGroup>()?.value) {
            it.cancel()
        }
    }
    dispatcher.queuedCalls().forEach {
        if (group === it.request().label<NetLabel.RequestGroup>()?.value) {
            it.cancel()
        }
    }
}
