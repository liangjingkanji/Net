package com.drake.net.exception

import okhttp3.Request

class NetUnknownHostException(
    request: Request,
    cause: Throwable? = null
) : NetException(request, cause = cause)