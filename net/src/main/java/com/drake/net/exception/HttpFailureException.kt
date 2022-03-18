package com.drake.net.exception

import okhttp3.Request

/**
 * 该类表示Http请求在服务器响应之前失败
 * @param request 请求信息
 * @param message 错误描述信息
 * @param cause 错误原因
 */
open class HttpFailureException(
    request: Request,
    message: String? = null,
    cause: Throwable? = null
) : NetException(request, message, cause)