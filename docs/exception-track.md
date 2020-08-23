Net对于网络发送的异常追踪非常详细, 不仅是网络的还是异步任务的异常处理都非常独到

<br>
!!! note
    只有网络错误就会在LogCat打印出异常信息, 并且跟随其地址信息


演示访问一个不存在的请求路径(但是URL存在)
```kotlin
scopeNetLife {
    // 这是一个错误的地址, 请查看LogCat的错误信息, 在[Convert]中你也可以进行自定义错误信息打印
    tv_fragment.text = Get<String>("error").await()
}
```

查看LogCat

<img src="https://i.imgur.com/5BpSWVv.png" width="100%"/>

这属于请求参数错误404, 将会打印出错误码以及请求的URL.

<br>
同时任何网络请求错误都将在LogCat查看, 并且Convert(转换器)和Interceptor(拦截器)中都可以通过`request/response`获取请求/响应的信息.