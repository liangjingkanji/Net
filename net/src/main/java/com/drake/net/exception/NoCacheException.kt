package com.drake.net.exception

import okhttp3.Request

/**
 * 该异常暂未实现, 属于保留异常
 */
class NoCacheException(
    request: Request,
    message: String? = null,
    cause: Throwable? = null
) : NetException(request, message, cause)