[net](../../index.md) / [com.drake.net.request](../index.md) / [BaseRequest](index.md) / [setPath](./set-path.md)

# setPath

`fun setPath(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, encoded: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

### Parameters

`path` - 如果其不包含http/https则会自动拼接[NetConfig.host](../../com.drake.net/-net-config/host.md)

`encoded` - 是否已经进行过URLEncoder编码