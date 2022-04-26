package com.drake.net.exception

import com.drake.net.cache.ForceCache
import okhttp3.Request

/**
 * 读取缓存失败
 * 仅当设置强制缓存模式[com.drake.net.cache.CacheMode.READ]和[com.drake.net.cache.CacheMode.REQUEST_THEN_READ]才会发生此异常
 * @param request 请求信息
 * @param message 错误描述信息
 * @param cause 错误原因
 */
class NoCacheException(
    request: Request,
    message: String? = null,
    cause: Throwable? = null
) : NetException(request, message, cause) {

    override fun getLocalizedMessage(): String {
        return "cacheKey = " + ForceCache.key(request) + " " + super.getLocalizedMessage()
    }
}