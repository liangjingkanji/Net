package com.drake.net.exception

import okhttp3.Request

/**
 * 请求过程中读取或者写入超时
 * @param request 请求信息
 * @param message 错误描述信息
 * @param cause 错误原因
 */
class NetSocketTimeoutException(
    request: Request,
    message: String? = null,
    cause: Throwable? = null
) : HttpFailureException(request, message, cause)