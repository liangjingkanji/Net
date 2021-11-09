package com.drake.net.exception

import okhttp3.Request

/**
 * 实现该接口表示Http请求失败
 */
open class HttpFailureException(
    request: Request,
    message: String? = null,
    cause: Throwable? = null
) : NetException(request, message, cause)