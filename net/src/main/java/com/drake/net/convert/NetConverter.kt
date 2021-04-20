/*
 * Copyright (C) 2018 Drake, Inc.
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

package com.drake.net.convert

import com.drake.net.exception.ConvertException
import com.drake.net.response.file
import okhttp3.Response
import okio.ByteString
import java.io.File
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
interface NetConverter {

    @Throws(Exception::class)
    fun <R> onConvert(succeed: Type, response: Response): R?

    companion object {
        /**
         * 返回数据为字符串内容
         */
        @JvmField
        val DEFAULT = object : NetConverter {

            override fun <R> onConvert(
                succeed: Type,
                response: Response
            ): R? {
                return when (succeed) {
                    String::class.java -> response.body?.string() as R
                    ByteString::class.java -> response.body?.byteString() as R
                    ByteArray::class.java -> response.body?.bytes() as R
                    Response::class.java -> response as R
                    File::class.java -> response.file() as R
                    else -> throw ConvertException(response, "The default converter does not support this type")
                }
            }
        }

    }
}