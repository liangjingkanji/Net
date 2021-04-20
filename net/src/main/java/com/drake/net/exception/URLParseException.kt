package com.drake.net.exception

/**
 * URL地址错误
 */
open class URLParseException(
    val url: String,
    cause: Throwable? = null
) : Exception(url, cause)