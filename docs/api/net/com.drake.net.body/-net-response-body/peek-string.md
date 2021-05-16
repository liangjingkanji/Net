[net](../../index.md) / [com.drake.net.body](../index.md) / [NetResponseBody](index.md) / [peekString](./peek-string.md)

# peekString

`fun peekString(byteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1024 * 1024 * 4, discard: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

复制一段指定长度的字符串内容

### Parameters

`byteCount` - 复制的字节长度. 如果-1则返回完整的字符串内容

`discard` - 如果实际长度大于指定长度则直接返回null. 可以保证数据完整性