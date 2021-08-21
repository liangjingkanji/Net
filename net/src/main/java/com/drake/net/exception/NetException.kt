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

package com.drake.net.exception

import okhttp3.Request
import java.io.IOException


/**
 * Net网络异常
 * @param message 异常信息
 */
open class NetException(
    open val request: Request,
    message: String? = null,
    cause: Throwable? = null
) : IOException(message, cause) {

    override fun getLocalizedMessage(): String? {
        return if (message != null) "$message (${request.url})" else "(${request.url})"
    }
}