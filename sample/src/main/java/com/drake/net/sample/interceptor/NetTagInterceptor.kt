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

@file:Suppress("ControlFlowWithEmptyBody")

package com.drake.net.sample.interceptor

import com.drake.net.tag.REQUEST
import com.drake.net.tag.RESPONSE
import com.drake.net.tag.TAG
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.connect.Interceptor
import com.yanzhenjie.kalle.connect.http.Chain

class NetTagInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()

        val tag = request.tag() as? TAG

        tag?.let {
            if (it.contains(REQUEST)) {
                // 可以打印响应体或者其他逻辑
            }

            if (it.contains(RESPONSE)) {
                // 可以打印请求体或者其他逻辑
            }
        }

        return chain.proceed(request)
    }
}