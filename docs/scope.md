Net的网络请求本身支持在官方的自带的作用域内使用, 但是考虑到完整的生命周期和错误处理等推荐使用Net内部定义的作用域.

> 全部使用顶层扩展函数

本质上Net的请求动作函数返回的是一个Deferred对象. 可以在任何协程作用域内执行. 但是协程默认的作用域没有生命周期进行处理.

这里建议你使用Net提供的创建作用域函数.

## 异步任务的作用域

创建可以捕捉异常的协程作用域, 但是不会触发`NetErrorHandler`(默认异常处理者). 该作用域于一般用于普通的异步任务

|函数|描述|
|-|-|
|`scope`|创建最基础的作用域, 所有作用域都包含异常捕捉|
|`scopeLife`|创建跟随生命周期取消的作用域|
|`ViewModel.scopeLife`|创建跟随ViewModel生命周期的作用域, [如何在ViewModel创建作用域](view-model.md)|


## 网络请求的作用域

网络请求的作用域可以根据生命周期自动取消网络请求, 发生错误也会自动弹出吐司(可以自定义或者取消), 并且具备一些场景的特殊功能(例如加载对话框, 缺省页, 下拉刷新等)

网络请求的作用域比上面提到的异步任务的作用域多的区别就是

1. 发生错误会触发全局错误处理`NetErrorHandler`
2. 具备一些特殊场景功能, 比如自动下拉刷新, 自动显示加载库等

| 函数 | 描述 |
|-|-|
|`scopeNet`|创建自动处理网络错误的作用域|
|`scopeNetLife`|创建自动处理网络错误的作用域, 且包含跟随Activity或者Fragment生命周期|
|`scopeDialog`|创建自动加载对话框的作用域, 生命周期跟随对话框|
|`ViewModel.scopeNetLife`|创建跟随ViewModel生命周期的作用域, [如何在ViewModel创建作用域](view-model.md)|
|`PageRefreshLayout.scope`|创建跟随[PageRefreshLayout](https://github.com/liangjingkanji/BRV)生命周期的作用域|
|`StateLayout.scope`|创建跟随[StateLayout](https://github.com/liangjingkanji/BRV)生命周期的作用域|

<br>
> PageRefreshLayout/StateLayout 属于[BRV](https://github.com/liangjingkanji/BRV)框架中的布局, 用于支持[自动化缺省页/下拉刷新](auto-state.md)
<br>


> 如果想了解详细的协程使用方式, 可以查看我的一篇文章: [最全面的Kotlin协程: Coroutine/Channel/Flow 以及实际应用](https://juejin.im/post/6844904037586829320)

有时候可能面临嵌套的`scope*`函数或者作用域内有子作用域情况, 这个时候的生命周期是如何


## 嵌套Scope

```kotlin hl_lines="5"
scopeNet {
    val task = Post<String>("api0").await()

    scopeNet {
        val task = Post<String>("api0").await() // 此时发生请求错误
    }.catch {
        // A
    }
}.catch {
    // B
}
```

- 以下嵌套作用域错误将会仅发生在`A`处, 并被捕获, 同时不影响外部`scopeNet`的请求和异常捕获
- 两个`scopeNet`的异常抛出和捕获互不影响
- `scopeNet/scopeDialog/scope`等函数同理

## 子作用域

```kotlin hl_lines="7 10"
scopeNet {
    val await = Post<String>("api").await()

    launch {
       val task = Post<String>("api0").await()  // 此时发生请求错误
    }.invokeOnCompletion {
        // A
    }
}.catch {
     // B
}
```

- 这种情况 先执行`A`然后执行`B`, 并且都能捕获异常.
- 同时`scopeNet`发生错误也会导致`launch`内的请求被取消, `launch`发生错误也会导致`scopeNet`发生错误


