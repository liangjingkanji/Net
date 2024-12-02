创建不同协程作用域可以实现不同的功能

!!! Success "减少崩溃"
    Net所有作用域内抛出异常都会被捕获到, 可以防止应用崩溃 <br>

## 异步任务的作用域

可以捕捉异常的协程作用域, 用于构建普通异步任务

|函数|描述|
|-|-|
|`scope`|创建最基础的作用域|
|`scopeLife`|创建跟随生命周期自动取消的作用域|
|`ViewModel.scopeLife`|创建跟随ViewModel生命周期的作用域, [如何在ViewModel创建作用域](view-model.md)|


## 网络请求的作用域

除异步任务外还适用于网络请求场景的作用域, 对比上面`异步任务的作用域`区别:

1. 发生错误自动吐司(可以自定义或者取消)
2. 发生错误会触发全局错误处理`NetErrorHandler`
3. 具备一些特殊场景功能, 比如根据网络请求结果自动处理下拉刷新/上拉加载/缺省页/加载框的状态

| 函数 | 描述 |
|-|-|
|`scopeNet`|创建自动处理网络错误的作用域|
|`scopeNetLife`|创建自动处理网络错误的作用域, 且包含跟随Activity或者Fragment生命周期|
|`scopeDialog`|创建自动加载对话框的作用域, 生命周期跟随对话框|
|`ViewModel.scopeNetLife`|创建跟随ViewModel生命周期的作用域, [如何在ViewModel创建作用域](view-model.md)|
|`PageRefreshLayout.scope`|创建跟随[PageRefreshLayout](https://github.com/liangjingkanji/BRV)生命周期的作用域|
|`StateLayout.scope`|创建跟随[StateLayout](https://github.com/liangjingkanji/BRV)生命周期的作用域|

!!! Failure "区分函数接受者"
    优先跟随布局而非Activity生命周期, 所以PageRefreshLayout等onRefresh函数内请使用`scope`

!!! quote "第三方库支持"
    PageRefreshLayout/StateLayout 属于第三方开源项目 [BRV](https://github.com/liangjingkanji/BRV)
    框架中的布局, 可用于支持[自动化缺省页/下拉刷新](auto-state.md)<br>

如果想更了解协程使用方式,
可以阅读一篇文章: [最全面的Kotlin协程: Coroutine/Channel/Flow 以及实际应用](https://juejin.im/post/6844904037586829320)

## 嵌套作用域

有时候可能面临内嵌`scopeXX`函数(嵌套作用域), 这时候生命周期如下

```kotlin hl_lines="5"
scopeNetLife {
    val task = Post<String>("path").await()

    scopeNetLife {
        val task = Post<String>("path/error").await() // 此时发生请求错误
    }.catch {
        // A
    }
}.catch {
    // B
}
```

- 错误将在`A`处可以获取到, 且不影响外部`scopeNetLife`的请求
- 两个`scopeNetLife`的异常抛出和捕获互不影响
- `scopeXX()`等函数同理

## 子作用域

```kotlin hl_lines="7 10"
scopeNet {
    val await = Post<String>("path").await()

    launch {
       val task = Post<String>("path/error").await()  // 此时发生请求错误
    }.invokeOnCompletion {
        // A
    }
}.catch {
     // B
}
```

- 这种情况 先执行`A`然后执行`B`, 并且都能捕获异常.
- 同时`scopeNet`发生错误也会导致`launch`内的请求被取消, `launch`发生错误也会导致`scopeNet`发生错误


