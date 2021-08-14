Net支持在ViewModel中创建网络请求/异步任务, 并且在ViewModel被销毁时自动取消


## 使用
和一般网络请求没有区别, 在ViewModel中使用函数`scopeLife/scopeNetLife`这两个函数创建作用域即可, 具体介绍看[作用域](scope.md)


```kotlin
class UserViewModel : ViewModel() {

    // 用户信息
    var userInfo: MutableLiveData<String> = MutableLiveData()

    /**
     * 拉取用户信息, 会自动通知页面更新, 同时页面销毁会自动取消网络请求
     */
    fun fetchUserInfo() = scopeNetLife {
        userInfo.value = Get<String>("api").await()
    }
}
```


1. 不建议使用LiveData实现MVVM. 应当使用DataBinding. 但LiveData像ObservableField一样使用
1. 简单地业务直接在Activity/Fragment中进行请求会更加方便
1. ViewModel这个类本质是属于防止数据意外销毁或者桥接VM, 但不是每个页面都有这种需求

<br>

<img src="https://i.loli.net/2021/08/14/h56aU9iCswzqp4A.png" width="350"/>
