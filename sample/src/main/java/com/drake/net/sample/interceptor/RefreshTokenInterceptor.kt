package com.drake.net.sample.interceptor

import com.drake.net.Net
import com.drake.net.exception.ResponseException
import com.drake.net.sample.constants.Api
import com.drake.net.sample.constants.UserConfig
import com.drake.net.sample.model.TokenModel
import okhttp3.Interceptor
import okhttp3.Response


/**
 * 客户端token自动续期示例
 */
class RefreshTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return synchronized(RefreshTokenInterceptor::class.java) {
            if (response.code == 401 && UserConfig.isLogin && !request.url.encodedPath.contains(Api.Token)) {
                val tokenInfo = Net.get(Api.Token).execute<TokenModel>() // 同步请求token
                if (tokenInfo.isExpired) {
                    // token过期抛出异常, 由全局错误处理器处理, 在其中可以跳转到登陆界面提示用户重新登陆
                    throw ResponseException(response, "登录状态失效")
                } else {
                    UserConfig.token = tokenInfo.token
                }
                chain.proceed(request)
            } else {
                response
            }
        }
    }
}