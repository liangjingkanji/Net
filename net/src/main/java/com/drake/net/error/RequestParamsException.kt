/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：10/28/19 8:55 PM
 */

package com.drake.net.error

import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.exception.NetException

/**
 * 404
 */
class RequestParamsException(val code: Int,
                             request: Request) : NetException(request, code.toString())