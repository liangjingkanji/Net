Net虽然支持自动跟随生命周期取消网络请求, 绝大部分场景也足够. 但是有时还是需要手动取消, 例如取消下载文件.
<br>

Net取消协程作用域自动取消内部网络请求, 也支持任意位置取消指定网络请求.

```kotlin
downloadScope = scopeNetLife {
    // 下载文件
    Download("download", requireContext().filesDir.path).await()
}

downloadScope.cancel() // 取消下载
```
完整示例: [源码](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/ui/fragment/DownloadFileFragment.kt)


## 任意位置取消
发起请求的时候要求定义一个`UID`用于指定网络请求, 然后在需要的地方使用单例对象`NetCancel`取消请求.

创建请求
```kotlin
scopeNetLife {
    tv_fragment.text = Get<String>("api", uid = "请求用户信息").await()
}
```

然后根据uid取消网络请求
```kotlin
NetCancel.cancel("请求用户信息")
```

<br>

!!! note
    需要注意的是一旦为网络请求设置UID你就无法在作用域执行完毕自动取消网络请求了, 因为自动取消的原理就是使用作用域
    的上下文错误处理器来作为UID
    ```kotlin hl_lines="6"
    inline fun <reified M> CoroutineScope.Get(
        path: String,
        tag: Any? = null,
        cache: CacheMode = CacheMode.HTTP,
        absolutePath: Boolean = false,
        uid: Any? = coroutineContext[CoroutineExceptionHandler],
        noinline block: SimpleUrlRequest.Api.() -> Unit = {}
    ): Deferred<M> = async(Dispatchers.IO)
    ```