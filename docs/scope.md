Net的网络请求本身支持在官方的自带的作用域内使用, 但是考虑到完整的生命周期和错误处理等推荐使用Net内部定义的作用域.

<br>
全部使用顶层扩展函数

|函数|描述|
|-|-|
|`scope`|创建最基础的作用域, 所有作用域都包含异常捕捉|
|`scopeLife`|创建跟随生命周期取消的作用域|
|`scopeNet`|创建自动处理网络错误的作用域|
|`scopeNetLife`|创建自动处理网络错误的作用域, 且包含跟随生命周期|
|`scopeDialog`|创建自动加载对话框的作用域, 生命周期跟随对话框|
|`PageRefreshLayout.scope`|创建跟随[PageRefreshLayout](https://github.com/liangjingkanji/BRV)生命周期的作用域|
|`StateLayout.scope`|创建跟随[StateLayout](https://github.com/liangjingkanji/BRV)生命周期的作用域|

<br>

!!! note
    PageRefreshLayout/StateLayout 属于[BRV](https://github.com/liangjingkanji/BRV)框架中的布局, 用于支持[自动化缺省页/下拉刷新](auto-state.md)
<br>

## 捕捉异常/执行完成
```kotlin
scope {

}.catch {
    // 协程内部发生错误回调
}.finally {
    // 协程内全部执行完成回调(包括子协程)
}
```



