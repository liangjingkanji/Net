[net](../../index.md) / [com.drake.net.body](../index.md) / [okhttp3.RequestBody](index.md) / [peekString](./peek-string.md)

# peekString

`@JvmName("peekString") fun RequestBody?.peekString(byteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1024 * 1024, discard: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`

复制一段指定长度的字符串内容

### Parameters

`byteCount` - 复制的字节长度. 如果-1则返回完整的字符串内容