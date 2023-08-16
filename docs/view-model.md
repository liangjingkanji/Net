Net支持在ViewModel中创建网络请求/异步任务

!!! Warning "不推荐"
    1. 网络请求不一定要写在ViewModel <br>
    2. 网络请求不要写接口回调
    3. 可以在Activity中直接返回请求结果


## 自动生命周期

- [示例代码](https://github.com/liangjingkanji/Net/blob/c4d7c4cde6a34b9fa97a75cb357276b75432f8d1/sample/src/main/java/com/drake/net/sample/ui/fragment/ViewModelRequestFragment.kt)

使用`scopeXXLife()`创建作用域, 在ViewModel被销毁时自动取消请求

```kotlin
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
    fun fetchList(scope: CoroutineScope) = scope.Get<String>(Api.TEST)

    /**
     * 开始阻塞异步任务
     * 直接返回结果
     */
    suspend fun fetchPrecessData() = coroutineScope {
        val response = Get<String>(Api.TEST).await()
        response + "处理数据"
    }
}
```

