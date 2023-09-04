package com.drake.net.utils

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.checkOffsetAndCount
import okio.BufferedSink

/**
 * 创建ByteArray的RequestBody，注意，如果设置了文件的写入起始和结束，那么计算contentLength的时候会计算起始和结束的大小
 * 由于因为okhttp官方的contentLength()不计算offset，只返回byteCount.toLong()，会影响进度条的展示
 * @param contentType 如果为null则通过判断扩展名来生成MediaType
 * @param offset 写入文件从哪开始
 * @param byteCount 写入大小
 */
fun ByteArray.toRequestBody1(
    contentType: MediaType? = null,
    offset: Int = 0,
    byteCount: Int = size
): RequestBody {
    checkOffsetAndCount(size.toLong(), offset.toLong(), byteCount.toLong())
    return object : RequestBody() {
        override fun contentType() = contentType

        override fun contentLength() =
            if ((byteCount > 0 && byteCount > offset)) {
                (byteCount - offset).toLong()
            } else {
                (byteCount - offset).toLong()
            }

        override fun writeTo(sink: BufferedSink) {
            sink.write(this@toRequestBody1, offset, byteCount)
        }
    }
}