Net支持多个接口请求并发, 仅返回最快的请求结果, 剩余请求将被自动取消, 同样可以用于筛选掉无法响应的域名
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

假设并发的接口返回的数据类型不同  或者 想要监听最快请求返回的结果回调请使用[transform](api/net/com.drake.net.utils/kotlinx.coroutines.-coroutine-scope/fastest.md)函数

```kotlin
scopeNetLife {

    val fastest = Post<String>("api").transform {
        Log.d("日志", "Post") // 如果该接口最快则会回调这里
        it // 这里可以返回其他数据结果
    }

    val fastest2 = Get<String>("api").transform {
        Log.d("日志", "Get") // 如果该接口最快则会回调这里
        it
    }

    tv_fragment.text = fastest(fastest, fastest2)
}
```

有的场景下并发的接口返回的数据类型不同, 但是fastest只能返回一个类型, 我们可以使`transform`的回调函数返回结果都拥有一个共同的接口, 然后去类型判断

<br>

!!! note
    不要尝试使用这种方式来取代CDN加速