package com.drake.net.exception

import okhttp3.Request

class NetConnectException(
    request: Request,
    cause: Throwable? = null
) : NetException(request, cause = cause)