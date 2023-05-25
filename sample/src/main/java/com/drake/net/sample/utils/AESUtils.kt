package com.drake.net.sample.utils

import okio.ByteString
import okio.ByteString.Companion.decodeHex
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AESUtils {

    private const val KEY = "123456789"
    private const val IV = "123456789"

    fun encrypt(data: String): String {
        val key = KEY.decodeHex()
        val iv = IV.decodeHex()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = javax.crypto.spec.IvParameterSpec(iv.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(data.toByteArray())
        return ByteString.of(*encrypted).hex()
    }

    fun decrypt(data: String): String {
        val key = KEY.decodeHex()
        val iv = IV.decodeHex()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = javax.crypto.spec.IvParameterSpec(iv.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(data.decodeHex().toByteArray())
        return String(encrypted)
    }
}