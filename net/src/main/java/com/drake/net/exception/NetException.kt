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

package com.drake.net.exception

import okhttp3.Request
import java.io.IOException


/**
 * 表示为Net发生的网络异常
 * 在转换器[com.drake.net.convert.NetConverter]中抛出的异常如果没有继承该类都会被视为数据转换异常[ConvertException], 该类一般用于自定义异常
 * @param request 请求信息
 * @param message 错误描述信息
 * @param cause 错误原因
 */
open class NetException(
    open val request: Request,
    message: String? = null,
    cause: Throwable? = null,
) : IOException(message, cause) {

    var occurred: String = ""

    override fun getLocalizedMessage(): String? {
        return "${if (message == null) "" else message + " "}${request.url}$occurred"
    }
}