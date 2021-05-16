[net](../../index.md) / [com.drake.net.request](../index.md) / [BaseRequest](./index.md)

# BaseRequest

`abstract class BaseRequest`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `BaseRequest()` |

### Properties

| Name | Summary |
|---|---|
| [converter](converter.md) | `open var converter: `[`NetConverter`](../../com.drake.net.convert/-net-converter/index.md) |
| [downloadListeners](download-listeners.md) | `val downloadListeners: DownloadListeners` |
| [httpUrl](http-url.md) | `open var httpUrl: Builder` |
| [method](method.md) | `open var method: `[`Method`](../-method/index.md) |
| [okHttpClient](ok-http-client.md) | `open var okHttpClient: OkHttpClient` |
| [okHttpRequest](ok-http-request.md) | `open var okHttpRequest: Builder` |
| [tags](tags.md) | `open var tags: TagHashMap` |

### Functions

| Name | Summary |
|---|---|
| [addDownloadListener](add-download-listener.md) | 下载监听器`fun addDownloadListener(progressListener: `[`ProgressListener`](../../com.drake.net.interfaces/-progress-listener/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [addHeader](add-header.md) | 添加请求头 如果已存在相同`name`的请求头会添加而不会覆盖, 因为请求头本身存在多个值`fun addHeader(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [buildRequest](build-request.md) | `open fun buildRequest(): Request` |
| [enqueue](enqueue.md) | `fun enqueue(block: Callback): Call` |
| [execute](execute.md) | 执行请求`fun <R> execute(): R` |
| [param](param.md) | `abstract fun param(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, encoded: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`abstract fun param(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`Number`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`abstract fun param(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeHeader](remove-header.md) | 删除请求头`fun removeHeader(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setCacheControl](set-cache-control.md) | 设置请求头的缓存控制`fun setCacheControl(cacheControl: CacheControl): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setClient](set-client.md) | 修改当前Request的OkHttpClient配置, 不会影响全局默认的OkHttpClient`fun setClient(block: Builder.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setDownloadDir](set-download-dir.md) | 下载文件的保存目录`fun setDownloadDir(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`fun setDownloadDir(name: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setDownloadFileName](set-download-file-name.md) | 下载文件名`fun setDownloadFileName(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setDownloadFileNameConflict](set-download-file-name-conflict.md) | 假设下载文件路径已存在同名文件是否重命名, 例如`file_name(1).apk``fun setDownloadFileNameConflict(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setDownloadFileNameDecode](set-download-file-name-decode.md) | 文件名称是否使用URL解码 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称`fun setDownloadFileNameDecode(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setDownloadMd5Verify](set-download-md5-verify.md) | 如果服务器返回 "Content-MD5"响应头和制定路径已经存在的文件MD5相同是否直接返回File`fun setDownloadMd5Verify(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setDownloadTempFile](set-download-temp-file.md) | 下载是否使用临时文件 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名 临时文件命名规则: 文件名 + .net-download     下载文件名: install.apk, 临时文件名: install.apk.net-download`fun setDownloadTempFile(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setGroup](set-group.md) | 分组`fun setGroup(group: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setHeader](set-header.md) | 设置请求头, 会覆盖请求头而不像[addHeader](add-header.md)是添加`fun setHeader(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setHeaders](set-headers.md) | 批量设置请求头`fun setHeaders(headers: Headers): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setId](set-id.md) | 唯一的Id`fun setId(id: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setLogRecord](set-log-record.md) | 是否启用日志记录器`fun setLogRecord(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setPath](set-path.md) | `fun setPath(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, encoded: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setQuery](set-query.md) | `fun setQuery(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, encoded: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setTag](set-tag.md) | 将一个任意对象添加到Request对象中, 一般用于在拦截器或者转换器中被获取到标签, 针对某个请求的特殊业务逻辑 使用`Request.tag()`获取标签`fun setTag(tag: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>添加标签 使用`Request.tag(name)`得到指定标签`fun setTag(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tag: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setUrl](set-url.md) | 设置一个Url字符串, 其参数不会和你初始化时设置的主域名[NetConfig.host](../../com.drake.net/-net-config/host.md)进行拼接 一般情况下我建议使用更为聪明的[setPath](set-path.md)`open fun setUrl(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`open fun setUrl(url: HttpUrl): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`open fun setUrl(url: `[`URL`](https://docs.oracle.com/javase/6/docs/api/java/net/URL.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [BodyRequest](../-body-request/index.md) | `open class BodyRequest : `[`BaseRequest`](./index.md) |
| [UrlRequest](../-url-request/index.md) | `open class UrlRequest : `[`BaseRequest`](./index.md) |
