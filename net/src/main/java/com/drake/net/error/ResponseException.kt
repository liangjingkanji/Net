/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.error

import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.exception.NetException

/**
 *  对应网络请求后台定义的错误信息
 * @param msg 网络请求错误信息
 * @param code 网络请求错误码
 * @param tag 应对错误码判断为错时但是后端又返回了需要使用的数据(建议后端修改). 一般在Convert中设置数据
 */
class ResponseException(
    val code: Int,
    val msg: String,
    request: Request
) : NetException(request, "$code $msg")
