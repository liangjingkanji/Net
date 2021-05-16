[net](../../index.md) / [com.drake.net.body](../index.md) / [NetResponseBody](./index.md)

# NetResponseBody

`class NetResponseBody : ResponseBody`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `NetResponseBody(responseBody: ResponseBody, progressListeners: `[`ConcurrentLinkedQueue`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html)`<`[`ProgressListener`](../../com.drake.net.interfaces/-progress-listener/index.md)`>? = null, complete: (() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null)` |

### Functions

| Name | Summary |
|---|---|
| [contentLength](content-length.md) | `fun contentLength(): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [contentType](content-type.md) | `fun contentType(): MediaType?` |
| [peekString](peek-string.md) | 复制一段指定长度的字符串内容`fun peekString(byteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1024 * 1024 * 4, discard: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [source](source.md) | `fun source(): BufferedSource` |
