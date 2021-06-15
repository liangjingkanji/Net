package com.drake.net.exception

class InstallException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)