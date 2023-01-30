/*
 * Copyright (C) 2018 Drake, https://github.com/liangjingkanji
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
 *
 */

package com.drake.net.sample.interceptor

import com.drake.net.Net
import com.drake.net.exception.ResponseException
import com.drake.net.sample.constants.UserConfig
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject


/**
 * 演示如何自动刷新token令牌
 */
class RefreshTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request) // 如果token失效

        return synchronized(RefreshTokenInterceptor::class.java) {
            if (response.code == 401 && UserConfig.isLogin && !request.url.pathSegments.contains("token")) {
                val json = Net.get("token").execute<String>() // 同步刷新token
                val jsonObject = JSONObject(json)
                if (jsonObject.getBoolean("isExpired")) {
                    // token刷新失败跳转到登录界面重新登录, 建议在错误处理器[NetErrorHandler]处理所有错误请求, 此处抛出异常即可
                    throw ResponseException(response, "登录状态失效")
                } else {
                    UserConfig.token = jsonObject.optString("token")
                }
                chain.proceed(request)
            } else {
                response
            }
        }
    }
}