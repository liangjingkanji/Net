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

package com.drake.net.utils

import android.webkit.MimeTypeMap
import com.drake.net.Net
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.ByteString.Companion.toByteString
import okio.source
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest

/**
 * 返回文件的MD5值
 * @param base64 是否将md5值进行base64编码, 否则将返回hex编码
 */
fun File.md5(base64: Boolean = false): String? {
    try {
        val fileInputStream = FileInputStream(this)
        val digestInputStream = DigestInputStream(fileInputStream, MessageDigest.getInstance("MD5"))
        val buffer = ByteArray(1024 * 256)
        digestInputStream.use {
            while (true) if (digestInputStream.read(buffer) <= 0) break
        }
        val md5 = digestInputStream.messageDigest.digest()
        return if (base64) {
            md5.toByteString().base64()
        } else {
            md5.toByteString().hex()
        }
    } catch (e: IOException) {
        Net.debug(e)
    }
    return null
}

/**
 * 返回字符串的MD5值
 * @param position 返回16位还是32位
 */
fun String.md5(position: Int = 16): String? {
    try {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.toByteArray(Charset.forName("utf-8")))
        return if (position == 16) digest.toByteString().hex()
            .substring(8, 24) else digest.toByteString().hex()
    } catch (e: IOException) {
        Net.debug(e)
    }
    return null
}

/**
 * 返回文件的MediaType值, 如果不存在返回null
 */
fun File.mediaType(): MediaType? {
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(absolutePath)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)?.toMediaTypeOrNull()
}

/**
 * 创建File的RequestBody，注意，如果设置了文件的写入起始和结束，那么计算contentLength的时候会计算起始和结束的大小
 * @param contentType 如果为null则通过判断扩展名来生成MediaType
 * @param offset 写入文件从哪开始
 * @param byteCount 写入大小
 */
fun File.toRequestBody(
    contentType: MediaType? = null,
    offset: Long = 0,
    byteCount: Long = 0
): RequestBody {
    val fileMediaType = contentType ?: mediaType()
    return object : RequestBody() {

        override fun contentType(): MediaType? {
            return fileMediaType
        }

        override fun contentLength() =
            if ((byteCount > 0 && byteCount > offset)) {
                byteCount - offset
            } else {
                length() - offset
            }

        override fun writeTo(sink: BufferedSink) {
            source().use { source ->
                if (offset > 0) {
                    source.buffer().skip(offset)
                }
                if (byteCount > 0 && byteCount > offset) {
                    sink.write(source, byteCount)
                } else {
                    sink.writeAll(source)
                }
            }
        }
    }
}