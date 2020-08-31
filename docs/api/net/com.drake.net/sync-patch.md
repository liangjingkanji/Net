[net](../index.md) / [com.drake.net](index.md) / [syncPatch](./sync-patch.md)

# syncPatch

`inline fun <reified M> syncPatch(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tag: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, cache: CacheMode = CacheMode.HTTP, absolutePath: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, uid: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, noinline block: Api.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): M`

同步网络请求

### Parameters

`path` - String 请求路径, 非绝对路径会加上[NetConfig.host](-net-config/host.md)为前缀

`tag` - 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断

`absolutePath` - 请求路径是否是绝对路径

`uid` - 表示请求的唯一id, 和[NetCancel.cancel](#)中的uid一致, 一般用于指定取消网络请求的唯一id

`block` - 函数中可以配置请求参数