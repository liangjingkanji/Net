[net](../../index.md) / [com.drake.net.request](../index.md) / [BaseRequest](index.md) / [setDownloadMd5Verify](./set-download-md5-verify.md)

# setDownloadMd5Verify

`fun setDownloadMd5Verify(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

如果服务器返回 "Content-MD5"响应头和制定路径已经存在的文件MD5相同是否直接返回File

