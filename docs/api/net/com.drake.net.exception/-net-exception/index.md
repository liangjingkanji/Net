[net](../../index.md) / [com.drake.net.exception](../index.md) / [NetException](./index.md)

# NetException

`open class NetException : `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html)

Net网络异常

### Parameters

`request` - 请求信息

`info` - 异常信息

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Net网络异常`NetException(request: Request, info: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, cause: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [info](info.md) | 异常信息`val info: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [request](request.md) | 请求信息`val request: Request` |

### Functions

| Name | Summary |
|---|---|
| [getLocalizedMessage](get-localized-message.md) | `open fun getLocalizedMessage(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Inheritors

| Name | Summary |
|---|---|
| [ConvertException](../-convert-exception/index.md) | 转换数据异常`class ConvertException : `[`NetException`](./index.md) |
| [DownloadFileException](../-download-file-exception/index.md) | 下载文件异常`class DownloadFileException : `[`NetException`](./index.md) |
| [NetConnectException](../-net-connect-exception/index.md) | `class NetConnectException : `[`NetException`](./index.md) |
| [NetSocketTimeoutException](../-net-socket-timeout-exception/index.md) | `class NetSocketTimeoutException : `[`NetException`](./index.md) |
| [NetUnknownHostException](../-net-unknown-host-exception/index.md) | `class NetUnknownHostException : `[`NetException`](./index.md) |
| [NoCacheException](../-no-cache-exception/index.md) | `class NoCacheException : `[`NetException`](./index.md) |
| [RequestParamsException](../-request-params-exception/index.md) | 400 - 499 客户端请求异常`class RequestParamsException : `[`NetException`](./index.md) |
| [ResponseException](../-response-exception/index.md) | 如果返回200但是返回数据不符合业务要求可以抛出该异常`class ResponseException : `[`NetException`](./index.md) |
| [ServerResponseException](../-server-response-exception/index.md) | = 500 服务器异常`class ServerResponseException : `[`NetException`](./index.md) |
