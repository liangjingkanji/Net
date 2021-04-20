[net](../../index.md) / [com.drake.net.interceptor](../index.md) / [RequestInterceptor](index.md) / [interceptor](./interceptor.md)

# interceptor

`abstract fun interceptor(request: `[`BaseRequest`](../../com.drake.net.request/-base-request/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

当你使用Net发起请求的时候就会触发该拦截器
该拦截器属于轻量级不具备重发的功能, 一般用于请求参数的修改
请勿在这里进行请求重发可能会导致死循环

