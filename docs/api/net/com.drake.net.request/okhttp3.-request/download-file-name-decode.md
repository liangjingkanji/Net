[net](../../index.md) / [com.drake.net.request](../index.md) / [okhttp3.Request](index.md) / [downloadFileNameDecode](./download-file-name-decode.md)

# downloadFileNameDecode

`fun Request.downloadFileNameDecode(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?`

下载的文件名称是否解码
例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称

