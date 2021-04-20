[net](../../index.md) / [com.drake.net.response](../index.md) / [okhttp3.Response](index.md) / [fileName](./file-name.md)

# fileName

`fun Response.fileName(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

按照以下顺序返回最终的下载文件的名称

1. 指定文件名
2. 响应头文件名
3. 请求URL路径
4. 时间戳
