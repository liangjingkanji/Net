package com.drake.net.sample.mock

import android.util.Log
import com.drake.engine.base.app
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import okhttp3.internal.closeQuietly
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
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
        return when (request.requestUrl?.encodedPath ?: "") {
            Api.TEST -> getString("Request Success : ${request.method}")
            Api.DELAY -> getString("Request Success : ${request.method}").setBodyDelay(2, TimeUnit.SECONDS)
            Api.UPLOAD -> uploadFile(request)
            Api.GAME -> getRawResponse(R.raw.game)
            Api.DATA -> getRawResponse(R.raw.data)
            Api.ARRAY -> getRawResponse(R.raw.array)
            Api.USER_INFO -> getRawResponse(R.raw.user)
            Api.CONFIG -> getRawResponse(R.raw.user)
            Api.TIME -> getString(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())).setBodyDelay(1, TimeUnit.SECONDS)
            else -> MockResponse().setResponseCode(404)
        }
    }

    private fun getString(text: String): MockResponse {
        return MockResponse().setHeader("Content-Type", "text/plain").setBody(text)
    }

    // 将接口上传的文件复制到应用缓存目录
    private fun uploadFile(req: RecordedRequest): MockResponse {
        val file = File(app.cacheDir.absolutePath, "uploadFile.apk")
        val source = req.body
        file.createNewFile()
        val sink = file.sink().buffer()
        sink.writeAll(source)
        sink.closeQuietly()
        source.closeQuietly()
        return MockResponse().setHeader("Content-Type", "text/plain").setBody("Upload success")
    }

    private fun getRawResponse(rawId: Int, delay: Long = 500): MockResponse {
        val buf = app.resources.openRawResource(rawId).source().buffer().readUtf8()
        return MockResponse()
            .setHeader("Content-Type", "application/json; charset=utf-8")
            .setBodyDelay(delay, TimeUnit.MILLISECONDS)
            .setBody(buf)
    }
}