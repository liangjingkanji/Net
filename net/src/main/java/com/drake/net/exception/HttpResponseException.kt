package com.drake.net.exception

import okhttp3.Response

/**
 * 实现该接口表示Http请求成功
 * @see ResponseException HttpStatusCode 200...299
 * @see RequestParamsException HttpStatusCode 400...499
 * @see ServerResponseException HttpStatusCode 500...599
 */
open class HttpResponseException(
    open val response: Response,
    message: String? = null,
    cause: Throwable? = null
) : NetException(response.request, message, cause)