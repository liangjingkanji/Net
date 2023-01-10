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
                    srv.start(8090)
                } catch (e: Exception) {
                    Log.e("日志", "MOCK服务启动失败", e)
                }
            }
        }
    }

    override fun dispatch(request: RecordedRequest): MockResponse {
        val response = MockResponse().setHeader("Content-Type", "application/json; charset=utf-8")

        return when (request.path) {
            Api.TEST -> response.setBody("Request Success : ${request.method}")
            Api.DELAY -> response.setBodyDelay(2, TimeUnit.SECONDS).setBody("Request Success : ${request.method}")
            Api.UPLOAD -> response.setBodyDelay(1, TimeUnit.SECONDS).setBody("Upload Success")
            Api.GAME -> {
                val buf = app.resources.openRawResource(R.raw.game).source().buffer().readUtf8()
                response.setBodyDelay(1, TimeUnit.SECONDS).setBody(buf)
            }
            else -> response.setResponseCode(404)
        }
    }
}