package com.drake.net.exception

import okhttp3.Response

/**
 * 下载文件异常
 */
class DownloadFileException(
    response: Response,
    message: String? = null,
    cause: Throwable? = null,
    var tag: Any? = null
) : HttpResponseException(response, message, cause)