package com.drake.net.sample.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.drake.net.Get
import com.drake.net.sample.constants.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope


/**
 * 强烈建议不要封装作用域, 封装异步任务即可, 作用域灵活随用随写(Activity/Fragment都可以写作用域)
 *
 * 不要企图把所有逻辑代码都写在ViewModel中就觉得自己会写MVVM了, 给代码换个位置不叫架构设计, 特别是还增加一堆无效代码情况下
 */
class UserViewModel : ViewModel() {

    // 用户信息
    var userInfo: MutableLiveData<String> = MutableLiveData()

    var updateTime: Long = 0

    /**
     * 拉取用户信息, 只能监听返回结果, 仅当外部调用对象不在乎返回结果时使用
     * 会自动通知页面更新: 因为使用LiveData将请求结果回调出去, 建议将该liveData对象直接使用DataBinding绑定到页面上, 就会自动触发UI
     * 同时页面销毁会自动取消网络请求: 因为他使用`scopeNetLife`. 生命周期跟随当前viewModel
     *
     * 本质上我并不推荐将Scope定义在ViewModel中(仅仅换个位置要多写很多代码), 特别是妄图在ViewModel中请求网络却要求更新UI
     */
    fun fetchUserInfo() = scopeNetLife {
        userInfo.value = Get<String>(Api.GAME).await()
        updateTime = System.currentTimeMillis()
    }

    /**
     * 非阻塞异步任务
     *  返回Deferred, 调用await()才会返回结果. 调用即执行任务
     */
    fun fetchList(scope: CoroutineScope) = scope.Get<String>(Api.TEST)

    /**
     * 阻塞异步任务
     * 直接返回结果
     */
    suspend fun fetchPrecessData() = coroutineScope {
        val response = Get<String>(Api.TEST).await()
        response + "处理数据"
    }
}