Net的协程作用域会自动处理协程生命周期

在上章节已经介绍如何发起并发网络请求, 这里演示同时(并发)请求`一万次`, 然后取消全部

```kotlin
val job = scopeNetLife {
    repeat(10000) {
        // 这里将返回的数据显示在TextView上
        launch {
            tv.text = Get<String>(Api.PATH).await()
        }
    }
}
```


等待五秒后取消请求
```kotlin
thread {
    Thread.sleep(5000) // 等待五秒
    job.cancel()
}
```

<br>

Net主要使用协程请求, 但也支持其他方式发起请求

- [同步请求](sync-request.md)
- [回调请求](callback.md)
- 协程请求
