package com.drake.net.exception

import okhttp3.Request

class NoCacheException(
    request: Request,
    message: String? = null,
    cause: Throwable? = null
) : NetException(request, message, cause)