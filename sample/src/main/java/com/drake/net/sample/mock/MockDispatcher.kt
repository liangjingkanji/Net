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
 */

package com.drake.net.sample.mock

import android.util.Log
import com.drake.engine.base.app
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MockDispatcher : Dispatcher() {

    companion object {
        fun initialize() {
            val srv = MockWebServer()
            srv.dispatcher = MockDispatcher()
            thread {
                try {
                    srv.start(8091)
                } catch (e: Exception) {
                    Log.e("日志", "MOCK服务启动失败", e)
                }
            }
        }
    }

    override fun dispatch(request: RecordedRequest): MockResponse {
        var path = request.path
        if (path != null) {
            path = path.substringBefore("?") // 剔除URL参数
        }
        return when (path) {
            Api.TEST -> MockResponse().setHeader("Content-Type", "text/plain").setBody("Request Success : ${request.method}")
            Api.DELAY -> MockResponse().setBodyDelay(2, TimeUnit.SECONDS).setHeader("Content-Type", "text/plain").setBody("Request Success : ${request.method}")
            Api.UPLOAD -> MockResponse().setBodyDelay(1, TimeUnit.SECONDS).setHeader("Content-Type", "text/plain").setBody("Upload Success")
            Api.GAME -> getRawResponse(R.raw.game)
            Api.DATA -> getRawResponse(R.raw.data)
            Api.ARRAY -> getRawResponse(R.raw.array)
            else -> MockResponse().setResponseCode(404)
        }
    }

    private fun getRawResponse(rawId: Int, delay: Long = 500): MockResponse {
        val buf = app.resources.openRawResource(rawId).source().buffer().readUtf8()
        return MockResponse()
            .setHeader("Content-Type", "application/json; charset=utf-8")
            .setBodyDelay(delay, TimeUnit.MILLISECONDS)
            .setBody(buf)
    }
}