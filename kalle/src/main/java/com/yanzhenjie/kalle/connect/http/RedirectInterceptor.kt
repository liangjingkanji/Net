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
package com.yanzhenjie.kalle.connect.http

import com.yanzhenjie.kalle.*
import com.yanzhenjie.kalle.connect.Interceptor
import com.yanzhenjie.kalle.util.IOUtils
import java.io.IOException

class RedirectInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return chain.redirect(request, response)
    }

    /**
     * 重定向
     */
    private fun Chain.redirect(request: Request, response: Response): Response {
        return if (response.isRedirect) {
            val oldUrl = request.url()
            val url = oldUrl.location(response.headers().location)
            val headers = request.headers()
            headers.remove(Headers.KEY_COOKIE)
            val method = request.method()
            val newRequest: Request
            newRequest = if (method.allowBody()) {
                BodyRequest.newBuilder(url, method)
                    .setHeaders(headers)
                    .setParams(request.copyParams())
                    .body(request.body())
                    .build()
            } else {
                UrlRequest.newBuilder(url, method)
                    .setHeaders(headers)
                    .build()
            }
            IOUtils.closeQuietly(response)
            redirect(newRequest, proceed(newRequest))
        } else response
    }


}