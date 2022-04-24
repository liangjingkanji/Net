Net中网络请求导致的异常都会在LogCat中打印, 同时被全局的NetErrorHandler的onError拦截到(除非catch住)


演示访问一个不存在的请求路径
```kotlin
scopeNetLife {
    tvFragment.text = Get<String>("https://githuberror.com/liangjingkanji/Net/").await()
}
```

查看LogCat可以看到异常堆栈信息, 这属于URL未知异常

<img src="https://s2.loli.net/2022/04/24/JVT2kP1Kn5B6Uqd.png" width="1000"/>

截图可以看到有具体的URL和请求代码位置


### 关闭日志

在初始化时候可以关闭日志打印

```kotlin
NetConfig.initialize("http://github.com/") {
    setDebug(false) // 关闭日志, 我们一般使用 BuildConfig.DEBUG
}
```

或者设置字段`NetConfig.debug`的值