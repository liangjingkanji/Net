Net虽然支持自动跟随生命周期取消网络请求, 绝大部分场景也足够. 但是有时还是需要手动取消, 例如取消下载文件.
<br>

Net取消协程作用域自动取消内部网络请求, 也支持任意位置取消指定网络请求.

```kotlin
downloadScope = scopeNetLife {
    // 下载文件
    val file = Get<File>("download").await()
}

downloadScope.cancel() // 取消下载
```
完整示例: [源码](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/ui/fragment/DownloadFileFragment.kt)


## 任意位置取消
发起请求的时候要求定义一个`Id`用于指定网络请求, 然后在需要的地方使用单例对象`Net.cancelId`取消请求.

创建请求
```kotlin
scopeNetLife {
    tvFragment.text = Get<String>("api"){
        setId("请求用户信息")
    }.await()
}
```

然后根据Id取消网络请求
```kotlin
Net.cancelId("请求用户信息")

Net.cancelGroup("请求分组名称") // 设置分组
```

Group和Id在使用场景上有所区别, 预期上Group允许重复赋值给多个请求, Id仅允许赋值给一个请求, 但实际上都允许重复赋值 <br>
在作用域中发起请求时会默认使用协程错误处理器作为Group: `setGroup(coroutineContext[CoroutineExceptionHandler])` <br>
如果你覆盖Group会导致协程结束不会自动取消请求

<br>