package com.drake.net.body

import com.drake.net.interfaces.ProgressListener
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import java.util.concurrent.ConcurrentLinkedQueue

fun RequestBody.toNetRequestBody(listeners: ConcurrentLinkedQueue<ProgressListener>? = null) = run {
    NetRequestBody(this, listeners)
}

fun ResponseBody.toNetResponseBody(
    listeners: ConcurrentLinkedQueue<ProgressListener>? = null,
    complete: (() -> Unit)? = null
) = run { NetResponseBody(this, listeners, complete) }

/**
 * 复制一段指定长度的字符串内容
 * @param byteCount 复制的字节长度. 如果-1则返回完整的字符串内容
 */
@JvmName("peekString")
fun RequestBody?.peekString(byteCount: Long = 1024 * 1024, discard: Boolean = false): String? {
    return when {
        this == null -> return null
        this is NetRequestBody -> peekString(byteCount, discard)
        else -> {
            val buffer = Buffer()
            writeTo(buffer)
            if (discard && buffer.size > byteCount) return ""
            val byteCountFinal = if (byteCount < 0) buffer.size else minOf(buffer.size, byteCount)
            buffer.readUtf8(byteCountFinal)
        }
    }
}

/**
 * 复制一段指定长度的字符串内容
 * @param byteCount 复制的字节长度. 如果-1则返回完整的字符串内容
 */
fun ResponseBody?.peekString(byteCount: Long = 1024 * 1024 * 4, discard: Boolean = false): String? {
    return when {
        this == null -> return null
        this is NetResponseBody -> peekString(byteCount, discard)
        else -> {
            val peeked = source().peek()
            val buffer = Buffer()
            peeked.request(byteCount)
            val byteCountFinal =
                if (byteCount < 0) peeked.buffer.size else minOf(byteCount, peeked.buffer.size)
            buffer.write(peeked, byteCountFinal)
            if (discard && buffer.size > byteCount) return ""
            buffer.asResponseBody(contentType(), byteCountFinal).string()
        }
    }
}