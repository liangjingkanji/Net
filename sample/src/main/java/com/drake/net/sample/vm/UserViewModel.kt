package com.drake.net.sample.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.drake.net.sample.constants.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope


/**
 * 不要将请求结果抛来抛去, 增加代码复杂度
 */
class UserViewModel : ViewModel() {

    // 用户信息
    var userInfo: MutableLiveData<String> = MutableLiveData()

    /**
     * 使用LiveData接受请求结果, 将该liveData直接使用DataBinding绑定到页面上, 会在请求成功自动更新视图
     */
    fun fetchUserInfo() = scopeNetLife {
        userInfo.value = Get<String>(Api.GAME).await()
    }

    /**
     * 开始非阻塞异步任务
     *  返回Deferred, 调用await()才会返回结果
     */
    fun fetchList(scope: CoroutineScope) = scope.Get<String>(Api.TEXT)

    /**
     * 开始阻塞异步任务
     * 直接返回结果
     */
    suspend fun fetchPrecessData() = coroutineScope {
        val response = Get<String>(Api.TEXT).await()
        response + "处理数据"
    }
}