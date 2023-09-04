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

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.documentfile.provider.DocumentFile
import com.drake.net.NetConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.buffer
import okio.source
import java.io.FileNotFoundException

fun Uri.fileName(): String? {
    return DocumentFile.fromSingleUri(NetConfig.app, this)?.name
}

fun Uri.mediaType(): MediaType? {
    val fileName = DocumentFile.fromSingleUri(NetConfig.app, this)?.name
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileName)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)?.toMediaTypeOrNull()
}

/**
 * 当Uri指向的文件不存在时将抛出异常[FileNotFoundException]
 */
@Throws(FileNotFoundException::class)
fun Uri.toRequestBody(
    startRange: Long = 0,
    endRange: Long = 0
): RequestBody {
    val document = DocumentFile.fromSingleUri(NetConfig.app, this)
    val contentResolver = NetConfig.app.contentResolver
    val contentLength = document?.length() ?: -1L
    val contentType = mediaType()
    return object : RequestBody() {
        override fun contentType(): MediaType? {
            return contentType
        }

        override fun contentLength() = contentLength

        override fun writeTo(sink: BufferedSink) {
            contentResolver.openInputStream(this@toRequestBody)?.use {
                it.source().use { source ->
                    if (startRange > 0) {
                        source.buffer().skip(startRange)
                    }
                    if (endRange > 0 && endRange > startRange) {
                        sink.write(source, endRange)
                    } else {
                        sink.writeAll(source)
                    }
                }
            }
        }
    }
}