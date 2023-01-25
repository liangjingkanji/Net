/*
 * Copyright (C) 2018 Drake, https://github.com/liangjingkanji
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