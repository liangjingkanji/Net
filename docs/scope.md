Net的网络请求本身支持在官方的自带的作用域内使用, 但是考虑到完整的生命周期和错误处理等推荐使用Net内部定义的作用域.

<br>
全部使用顶层扩展函数


## 异步任务的作用域

快速创建可以捕捉异常的协程作用域

|函数|描述|
|-|-|
|`scope`|创建最基础的作用域, 所有作用域都包含异常捕捉|
|`scopeLife`|创建跟随生命周期取消的作用域|


## 网络请求的作用域

网络请求的作用域可以根据生命周期自动取消网络请求, 发生错误也会自动弹出吐司(可以自定义或者取消), 并且具备一些场景的特殊功能(例如加载对话框, 缺省页, 下拉刷新等)

| 函数 | 描述 |
|-|-|
|`scopeNet`|创建自动处理网络错误的作用域|
|`scopeNetLife`|创建自动处理网络错误的作用域, 且包含跟随生命周期|
|`scopeDialog`|创建自动加载对话框的作用域, 生命周期跟随对话框|
|`PageRefreshLayout.scope`|创建跟随[PageRefreshLayout](https://github.com/liangjingkanji/BRV)生命周期的作用域|
|`StateLayout.scope`|创建跟随[StateLayout](https://github.com/liangjingkanji/BRV)生命周期的作用域|

<br>

> PageRefreshLayout/StateLayout 属于[BRV](https://github.com/liangjingkanji/BRV)框架中的布局, 用于支持[自动化缺省页/下拉刷新](auto-state.md)
<br>

## 捕捉异常/执行完成
```kotlin
scope {
    // scope系列函数的这个大括号里面就是作用域
}.catch {
    // 协程内部发生错误回调
}.finally {
    // 协程内全部执行完成回调(包括子协程)
}
```

> 如果想了解详细的协程使用方式, 可以查看我的一篇文章: [最全面的Kotlin协程: Coroutine/Channel/Flow 以及实际应用](https://juejin.im/post/6844904037586829320)



