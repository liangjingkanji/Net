[net](../../index.md) / [com.drake.net.request](../index.md) / [BaseRequest](index.md) / [setUrl](./set-url.md)

# setUrl

`open fun setUrl(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

设置一个Url字符串, 其参数不会和你初始化时设置的主域名[NetConfig.host](../../com.drake.net/-net-config/host.md)进行拼接
一般情况下我建议使用更为聪明的[setPath](set-path.md)

`open fun setUrl(url: HttpUrl): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)
`open fun setUrl(url: `[`URL`](https://docs.oracle.com/javase/6/docs/api/java/net/URL.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)