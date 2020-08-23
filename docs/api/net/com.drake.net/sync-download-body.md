[net](../index.md) / [com.drake.net](index.md) / [syncDownloadBody](./sync-download-body.md)

# syncDownloadBody

`fun syncDownloadBody(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, dir: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = NetConfig.app.externalCacheDir!!.absolutePath, tag: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, absolutePath: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, method: RequestMethod = RequestMethod.GET, uid: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null, block: Api.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

同步文件下载, 以提交请求体方式(默认Post请求)

### Parameters

`path` - String 网络路径, 非绝对路径会加上[NetConfig.host](-net-config/host.md)为前缀

`tag` - 可以传递对象给Request请求

`absolutePath` - Path是否是绝对路径

`uid` - 表示请求的唯一id

`block` - 配置参数lambda