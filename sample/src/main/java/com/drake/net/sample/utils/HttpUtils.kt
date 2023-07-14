package com.drake.net.sample.utils

import com.drake.net.Get
import com.drake.net.sample.constants.Api
import com.drake.net.sample.model.ConfigModel
import com.drake.net.sample.model.UserInfoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope


/**
 * 常用的请求方法建议写到一个工具类中
 */
object HttpUtils {

    /**
     * 获取配置信息
     *
     * 本方法需要再调用await()才会返回结果, 属于异步方法
     */
    fun getConfigAsync(scope: CoroutineScope) = scope.Get<ConfigModel>(Api.CONFIG)

    /**
     * 获取用户信息
     * 阻塞返回可直接返回结果
     *
     * @param userId 如果为空表示请求自身用户信息
     */
    suspend fun getUser(userId: String? = null) = coroutineScope {
        Get<UserInfoModel>(Api.USER_INFO) {
            param("userId", userId)
        }.await()
    }

}