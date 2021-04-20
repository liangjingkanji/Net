[net](../../index.md) / [com.drake.net](../index.md) / [Net](index.md) / [get](./get.md)

# get

`fun get(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tag: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, block: (`[`UrlRequest`](../../com.drake.net.request/-url-request/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): <ERROR CLASS>`

同步网络请求

### Parameters

`path` - 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host](../-net-config/host.md)

`tag` - 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断

`block` - 函数中可以配置请求参数