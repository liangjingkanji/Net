/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.listener

import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.Response

interface NetListener {

    fun net(request: Request, response: Response)
    fun parse(body: String): String
}