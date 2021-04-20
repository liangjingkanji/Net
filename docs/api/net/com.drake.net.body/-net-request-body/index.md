[net](../../index.md) / [com.drake.net.body](../index.md) / [NetRequestBody](./index.md)

# NetRequestBody

`class NetRequestBody : RequestBody`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `NetRequestBody(requestBody: RequestBody, progressListeners: `[`ConcurrentLinkedQueue`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html)`<`[`ProgressListener`](../../com.drake.net.request/-progress-listener/index.md)`>? = null)` |

### Properties

| Name | Summary |
|---|---|
| [contentLength](content-length.md) | `var contentLength: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

### Functions

| Name | Summary |
|---|---|
| [contentLength](content-length.md) | `fun contentLength(): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [contentType](content-type.md) | `fun contentType(): MediaType?` |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [writeTo](write-to.md) | `fun writeTo(sink: BufferedSink): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
