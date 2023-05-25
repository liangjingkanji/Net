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

package com.drake.net.convert

import com.drake.net.exception.ConvertException
import com.drake.net.response.file
import okhttp3.Response
import okio.ByteString
import java.io.File
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
interface NetConverter {

    @Throws(Throwable::class)
    fun <R> onConvert(succeed: Type, response: Response): R?

    companion object DEFAULT : NetConverter {
        /**
         * 返回结果应当等于泛型对象, 可空
         * @param succeed 请求要求返回的泛型类型
         * @param response 请求响应对象
         */
        override fun <R> onConvert(succeed: Type, response: Response): R? {
            return when {
                succeed === String::class.java && response.isSuccessful -> response.body?.string() as R
                succeed === ByteString::class.java && response.isSuccessful -> response.body?.byteString() as R
                succeed is GenericArrayType && succeed.genericComponentType === Byte::class.java && response.isSuccessful -> response.body?.bytes() as R
                succeed === File::class.java && response.isSuccessful -> response.file() as R
                succeed === Response::class.java -> response as R
                else -> throw ConvertException(response, "An exception occurred while converting the NetConverter.DEFAULT")
            }
        }
    }
}