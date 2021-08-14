Net中网络请求导致的异常都会在LogCat中打印, 同时被全局的NetErrorHandler的onError拦截到

> Net的异常对象通常会携带Request或者Response对象. 可以精准定位到请求信息


演示访问一个不存在的请求路径(但是URL存在)
```kotlin
scopeNetLife {
    // 这是一个错误的地址, 请查看LogCat的错误信息, 在[Convert]中你也可以进行自定义错误信息打印
    tvFragment.text = Get<String>("error").await()
}
```

查看LogCat可以看到异常堆栈信息

<img src="https://i.loli.net/2021/08/14/TsEdIXkpbLFgOeS.png" width="650"/>

这属于请求参数错误404, 将会打印出错误码以及请求的URL.


### 关闭日志

在初始化时候可以关闭日志打印

```kotlin
NetConfig.init("http://github.com/") {
    setLog(false) // 关闭日志
}
```

或者设置字段`NetConfig.logEnabled`的值