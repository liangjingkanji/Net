package com.drake.net.utils

import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.*

/**
 * 返回文件的MD5值
 */
fun File.md5(): String? {
    try {
        val fileInputStream = FileInputStream(this)
        val digestInputStream = DigestInputStream(fileInputStream, MessageDigest.getInstance("MD5"))
        val buffer = ByteArray(1024 * 256)
        digestInputStream.use {
            while (true) if (digestInputStream.read(buffer) <= 0) break
        }
        val md5 = digestInputStream.messageDigest.digest()
        val stringBuilder = StringBuilder()
        for (b in md5) stringBuilder.append(String.format("%02X", b))
        return stringBuilder.toString().toLowerCase(Locale.ROOT)
    } catch (e: IOException) {
        e.printStackTrace()
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
 * 创建File的RequestBody
 * @param contentType 如果为null则通过判断扩展名来生成MediaType
 */
fun File.toRequestBody(contentType: MediaType? = null): RequestBody {
    return object : RequestBody() {
        override fun contentType() = contentType ?: mediaType()

        override fun contentLength() = length()

        override fun writeTo(sink: BufferedSink) {
            source().use { source -> sink.writeAll(source) }
        }
    }
}