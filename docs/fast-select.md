Net支持多个接口请求并发, 仅返回最快的请求结果, 剩余请求将被自动取消, 同样可以用于筛选掉无法请求的域名
<br>

!!! note
    接口请求错误被忽略(LogCat依然可以看到异常信息), 但如果所有请求全部异常则抛出最后一个请求的异常作为错误处理

示例
```kotlin
scopeNetLife {

    // 同时发起四个网络请求
    val deferred = Get<String>("api0") // 错误接口
    val deferred1 = Get<String>("api1") // 错误接口
    val deferred2 = Get<String>("api")
    val deferred3 = Post<String>("api")

    // 只返回最快的请求结果
    tv_fragment.text = fastSelect(deferred, deferred1, deferred2, deferred3)
}
```

<br>

!!! note
    不要尝试使用这种方式来取代CDN加速