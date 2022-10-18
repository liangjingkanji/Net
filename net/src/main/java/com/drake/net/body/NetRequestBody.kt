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
package com.drake.net.body

import android.os.SystemClock
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue

class NetRequestBody(
    private val requestBody: RequestBody,
    private val progressListeners: ConcurrentLinkedQueue<ProgressListener>? = null
) : RequestBody() {

    private val progress = Progress()
    val contentLength by lazy { requestBody.contentLength() }

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return contentLength
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (sink is Buffer ||
            sink.toString().contains("com.android.tools.profiler.support.network.HttpTracker\$OutputStreamTracker")) {
            requestBody.writeTo(sink)
        } else {
            val bufferedSink: BufferedSink = sink.toProgress().buffer()
            requestBody.writeTo(bufferedSink)
            bufferedSink.close()
        }
    }

    /**
     * 复制一段指定长度的字符串内容
     * @param byteCount 复制的字节长度. 如果-1则返回完整的字符串内容
     * @param discard 如果实际长度大于指定长度则直接返回null. 可以保证数据完整性
     */
    fun peekString(byteCount: Long = 1024 * 1024, discard: Boolean = false): String {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        if (discard && buffer.size > byteCount) return ""
        val byteCountFinal = if (byteCount < 0) buffer.size else minOf(buffer.size, byteCount)
        return buffer.readUtf8(byteCountFinal)
    }

    private fun Sink.toProgress() = object : ForwardingSink(this) {
        var writeByteCount = 0L

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            writeByteCount += byteCount
            if (progressListeners != null) {
                val currentElapsedTime = SystemClock.elapsedRealtime()
                progressListeners.forEach { progressListener ->
                    progressListener.intervalByteCount += byteCount
                    val currentInterval = currentElapsedTime - progressListener.elapsedTime
                    if (currentInterval >= progressListener.interval || writeByteCount == contentLength) {
                        progressListener.onProgress(
                            progress.apply {
                                currentByteCount = writeByteCount
                                totalByteCount = contentLength
                                intervalByteCount = progressListener.intervalByteCount
                                intervalTime = currentInterval
                            }
                        )
                        progressListener.elapsedTime = currentElapsedTime
                        progressListener.intervalByteCount = 0L
                    }
                }
            }
        }
    }
}