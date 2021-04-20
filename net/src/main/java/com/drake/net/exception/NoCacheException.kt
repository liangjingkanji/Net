package com.drake.net.exception

import okhttp3.Request

class NoCacheException(
    request: Request,
    info: String? = null,
    cause: Throwable? = null
) : NetException(request, info, cause)