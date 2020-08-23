[net](../../index.md) / [com.drake.net](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [DownloadImage](./-download-image.md)

# DownloadImage

`fun CoroutineScope.DownloadImage(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, with: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = -1, height: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = -1): Deferred<`[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`>`

异步下载图片, 图片宽高要求要么同时指定要么同时不指定
要求依赖 Glide

### Parameters

`url` - 请求图片的绝对路径

`with` - 图片宽度

`height` - 图片高度