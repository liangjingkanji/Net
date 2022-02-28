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