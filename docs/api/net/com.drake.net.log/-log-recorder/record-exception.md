[net](../../index.md) / [com.drake.net.log](../index.md) / [LogRecorder](index.md) / [recordException](./record-exception.md)

# recordException

`@JvmStatic fun recordException(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, requestMillis: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, code: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?, response: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, errorMessage: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

发送请求异常到记录器

### Parameters

`id` - 请求的唯一标识符

`requestMillis` - 请求时间毫秒值

`errorMessage` - 错误信息, 如果存在\n换行符, 仅接受最后一行