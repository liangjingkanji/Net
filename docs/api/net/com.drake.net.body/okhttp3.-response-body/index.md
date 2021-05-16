[net](../../index.md) / [com.drake.net.body](../index.md) / [okhttp3.ResponseBody](./index.md)

### Extensions for okhttp3.ResponseBody

| Name | Summary |
|---|---|
| [peekString](peek-string.md) | 复制一段指定长度的字符串内容`fun ResponseBody?.peekString(byteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1024 * 1024 * 4, discard: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [toNetResponseBody](to-net-response-body.md) | `fun ResponseBody.toNetResponseBody(request: Request, complete: (() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): <ERROR CLASS>` |
