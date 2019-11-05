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
class RequestParamsException(code: Int) : Throwable(code.toString())