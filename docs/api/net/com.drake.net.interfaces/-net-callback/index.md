[net](../../index.md) / [com.drake.net.interfaces](../index.md) / [NetCallback](./index.md)

# NetCallback

`abstract class NetCallback<T> : Callback`

提供泛型转换特性
相对于OkHttp的Callback新增三个回调函数: [onSuccess](on-error.md) [onComplete](on-complete.md)
这三个函数都运行在主线程上

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | 提供泛型转换特性 相对于OkHttp的Callback新增三个回调函数: [onSuccess](on-error.md) [onComplete](on-complete.md) 这三个函数都运行在主线程上`NetCallback()` |

### Functions

| Name | Summary |
|---|---|
| [onComplete](on-complete.md) | 请求完成`fun onComplete(call: Call, e: `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onError](on-error.md) | 请求错误`fun onError(call: Call, e: `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onFailure](on-failure.md) | `open fun onFailure(call: Call, e: `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onResponse](on-response.md) | `open fun onResponse(call: Call, response: Response): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onSuccess](on-success.md) | 请求成功`abstract fun onSuccess(call: Call, data: T): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
