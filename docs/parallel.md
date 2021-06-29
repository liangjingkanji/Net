Net在2.0开始引入协程来支持并发和异步, 虽然很多网络框架支持协程, 但是Net对于协程生命周期的控制算得上是独有.
并且Net不仅仅网络请求, 其也支持创建任何异步任务.

> 这里的`同时/并发/并行`统称为并发(具体是不是并行不需要开发者来考虑)

<br>
在上章节已经使用过了网络的并发请求

这里再演示同时(并发)请求百度网站`一万次`并且一次取消

```kotlin
val job = scopeNetLife {
    repeat(10000) {
        // 这里将返回的数据显示在TextView上
        tvFragment.text = Get<String>("http://www.baidu.com/").await()
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