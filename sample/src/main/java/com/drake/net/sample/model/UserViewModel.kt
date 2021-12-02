package com.drake.net.sample.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

class UserViewModel : ViewModel() {

    // 用户信息
    var userInfo: MutableLiveData<String> = MutableLiveData()

    /**
     * 拉取用户信息, 会自动通知页面更新, 同时页面销毁会自动取消网络请求
     * 其包含作用域, 生命周期跟随当前viewModel
     * scopeNetLife/scopeDialog不推荐写在ViewModel中
     */
    fun fetchUserInfo() = scopeNetLife {
        userInfo.value = Get<String>("api").await()
    }

    /** 返回Deferred, 可以灵活使用, 支持并发组合 */
    fun CoroutineScope.fetchList() = Get<String>("api")

    /** 直接返回数据, 会阻塞直至数据返回 */
    suspend fun fetchPrecessData() = coroutineScope {
        val response = Get<String>("api").await()
        response + "处理数据"
    }
}