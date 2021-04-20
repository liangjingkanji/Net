[net](../../index.md) / [com.drake.net.request](../index.md) / [okhttp3.Request](./index.md)

### Extensions for okhttp3.Request

| Name | Summary |
|---|---|
| [converter](converter.md) | 转换器`fun Request.converter(): `[`NetConverter`](../../com.drake.net.convert/-net-converter/index.md)`?` |
| [downloadConflictRename](download-conflict-rename.md) | 当指定下载目录存在同名文件是覆盖还是进行重命名, 重命名规则是: $文件名_($序号).$后缀`fun Request.downloadConflictRename(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [downloadFileDir](download-file-dir.md) | 下载文件目录`fun Request.downloadFileDir(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [downloadFileNameDecode](download-file-name-decode.md) | 下载的文件名称是否解码 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称`fun Request.downloadFileNameDecode(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [downloadListeners](download-listeners.md) | 下载监听器`fun Request.downloadListeners(): `[`ConcurrentLinkedQueue`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html)`<`[`ProgressListener`](../-progress-listener/index.md)`>?` |
| [downloadMd5Verify](download-md5-verify.md) | 是否进行校验文件md5, 如果校验则匹配上既马上返回文件而不会进行下载`fun Request.downloadMd5Verify(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [downloadTempFile](download-temp-file.md) | 下载是否使用临时文件 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名 临时文件命名规则: 文件名 + .net-download     下载文件名: install.apk, 临时文件名: install.apk.net-download`fun Request.downloadTempFile(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [fileName](file-name.md) | 下载文件名`fun Request.fileName(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [group](group.md) | 请求的分组名`fun Request.group(): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [id](id.md) | 请求的Id`fun Request.id(): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [label](label.md) | `fun <T> Request.label(): T?` |
| [tag](tag.md) | 请求标签`fun Request.tag(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [uploadListeners](upload-listeners.md) | 上传监听器`fun Request.uploadListeners(): `[`ConcurrentLinkedQueue`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html)`<`[`ProgressListener`](../-progress-listener/index.md)`>?` |
