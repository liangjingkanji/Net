package com.drake.net.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.*

/**
 * 返回文件的MD5值
 */
fun File?.md5(): String? {
    this ?: return null
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