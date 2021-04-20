[net](../../index.md) / [com.drake.net.request](../index.md) / [okhttp3.Request](index.md) / [downloadTempFile](./download-temp-file.md)

# downloadTempFile

`fun Request.downloadTempFile(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?`

下载是否使用临时文件
避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名
临时文件命名规则: 文件名 + .net-download
    下载文件名: install.apk, 临时文件名: install.apk.net-download

