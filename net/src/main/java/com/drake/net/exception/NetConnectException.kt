package com.drake.net.exception

import okhttp3.Request

class NetConnectException(
    request: Request,
    message: String? = null,
    cause: Throwable? = null
) : HttpFailureException(request, message, cause)