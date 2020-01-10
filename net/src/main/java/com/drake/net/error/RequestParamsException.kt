/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：10/28/19 8:55 PM
 */

package com.drake.net.error

/**
 * 404
 */
class RequestParamsException(
    val code: Int,
    val msg: String = "",
    val tag: Any? = null
) : Throwable("$code: $msg")