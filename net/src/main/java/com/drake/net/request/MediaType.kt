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

import okhttp3.MediaType


val MediaType.Companion.IMG: MediaType
    get() = "image/*".toMediaType()

val MediaType.Companion.GIF: MediaType
    get() = "image/gif".toMediaType()

val MediaType.Companion.JPEG: MediaType
    get() = "image/jpeg".toMediaType()

val MediaType.Companion.PNG: MediaType
    get() = "image/png".toMediaType()

val MediaType.Companion.MP4: MediaType
    get() = "video/mpeg".toMediaType()

val MediaType.Companion.TXT: MediaType
    get() = "text/plain".toMediaType()

val MediaType.Companion.JSON: MediaType
    get() = "application/json; charset=utf-8".toMediaType()

val MediaType.Companion.XML: MediaType
    get() = "application/xml".toMediaType()

val MediaType.Companion.HTML: MediaType
    get() = "text/html".toMediaType()

val MediaType.Companion.FORM: MediaType
    get() = "multipart/form-data".toMediaType()

val MediaType.Companion.URLENCODED: MediaType
    get() = "application/x-www-form-urlencoded".toMediaType()
