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

package com.drake.net.request

import okhttp3.MediaType.Companion.toMediaType

object MediaConst {

    val IMG = "image/*".toMediaType()

    val GIF = "image/gif".toMediaType()

    val JPEG = "image/jpeg".toMediaType()

    val PNG = "image/png".toMediaType()

    val MP4 = "video/mpeg".toMediaType()

    val TXT = "text/plain".toMediaType()

    val JSON = "application/json; charset=utf-8".toMediaType()

    val XML = "application/xml".toMediaType()

    val HTML = "text/html".toMediaType()

    val FORM = "multipart/form-data".toMediaType()

    val OCTET_STREAM = "application/octet-stream".toMediaType()

    val URLENCODED = "application/x-www-form-urlencoded".toMediaType()
}