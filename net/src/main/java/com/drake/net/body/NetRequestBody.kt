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
    private val body: RequestBody,
    private val progressListeners: ConcurrentLinkedQueue<ProgressListener>? = null
) : RequestBody() {

    private val progress = Progress()
    val contentLength by lazy { body.contentLength() }

    override fun contentType(): MediaType? {
        return body.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return contentLength
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (sink is Buffer ||
            sink.toString().contains("com.android.tools.profiler.support.network.HttpTracker\$OutputStreamTracker")) {
            body.writeTo(sink)
        } else {
            val bufferedSink: BufferedSink = sink.toProgress().buffer()
            body.writeTo(bufferedSink)
            bufferedSink.close()
        }
    }

    /**
     * 复制一段指定长度的字符串内容
     * @param byteCount 复制的字节长度, 允许超过实际长度, 如果-1则返回完整的字符串内容
     */
    fun peekBytes(byteCount: Long = 1024 * 1024): ByteString {
        val buffer = Buffer()
        body.writeTo(buffer)
        val maxSize = if (byteCount < 0) buffer.size else minOf(buffer.size, byteCount)
        return buffer.readByteString(maxSize)
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
                    if (!progress.finish && (writeByteCount == contentLength || currentInterval >= progressListener.interval)) {
                        if (writeByteCount == contentLength) {
                            progress.finish = true
                        }
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