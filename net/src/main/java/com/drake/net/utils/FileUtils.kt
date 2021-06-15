package com.drake.net.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.drake.net.NetConfig
import com.drake.net.exception.InstallException
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
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

/**
 * 安装APK
 */
fun File.install() {
    if (name.substringAfterLast(".").lowercase() != "apk") {
        throw InstallException("The file is not an apk file")
    }

    val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
            NetConfig.app,
            "${NetConfig.app.packageName}.fileProvider",
            this
        )
    } else {
        Uri.fromFile(this)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")

    try {
        NetConfig.app.startActivity(intent)
    } catch (e: Exception) {
        throw InstallException(cause = e)
    }
}