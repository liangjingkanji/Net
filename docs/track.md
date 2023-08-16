Net中网络请求异常会LogCat输出, 除非开发者修改全局错误处理


演示访问一个不存在的请求路径
```kotlin
scopeNetLife {
    tv.text = Get<String>("https://error.com/net/").await()
}
```

LogCat可以看到异常堆栈信息, 包含具体Url和代码位置

<img src="https://s2.loli.net/2022/04/24/JVT2kP1Kn5B6Uqd.png" width="1000"/>


### 关闭日志

可以关闭日志打印

```kotlin
NetConfig.initialize(Api.HOST, this) {
    setDebug(false) // 关闭日志, 一般使用 BuildConfig.DEBUG
}
```
