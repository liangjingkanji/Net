/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：1/13/20 1:23 PM
 */

package com.yanzhenjie.kalle.exception

import com.yanzhenjie.kalle.Request
import java.io.IOException


open class NetException(
    val request: Request,
    message: String? = null,
    cause: Throwable? = null
) : IOException(message + request.location(), cause)