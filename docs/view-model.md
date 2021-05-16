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


1. 不建议手动监听LiveData, 全是冗余代码
2. 建议 ViewModel + LiveData + DataBinding
3. 简化的可以直接在Activity/Fragment中进行请求会更加方便
4. DataBinding双向数据绑定才是MVVM的核心, LiveData
5. 我不认为每个页面都应当编写一个ViewModel, ViewModel这个类本质是属于防止数据意外销毁, 但是我们编写应用常常固定竖屏或者页面重新加载网络也是没有问题的.

<br>

<img src="https://i.imgur.com/4mG6P7a.png" width="350"/>
