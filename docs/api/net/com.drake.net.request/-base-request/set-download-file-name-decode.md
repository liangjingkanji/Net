[net](../../index.md) / [com.drake.net.request](../index.md) / [BaseRequest](index.md) / [setDownloadFileNameDecode](./set-download-file-name-decode.md)

# setDownloadFileNameDecode

`fun setDownloadFileNameDecode(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

文件名称是否使用URL解码
例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称

