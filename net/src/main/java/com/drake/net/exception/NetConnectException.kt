package com.drake.net.exception

import okhttp3.Request

/**
 * 连接错误
 * @param request 请求信息
 * @param message 错误描述信息
 * @param cause 错误原因
 */
class NetConnectException(
    request: Request,
    message: String? = null,
    cause: Throwable? = null
) : HttpFailureException(request, message, cause)