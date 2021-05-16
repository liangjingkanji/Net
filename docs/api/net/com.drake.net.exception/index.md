[net](../index.md) / [com.drake.net.exception](./index.md)

## Package com.drake.net.exception

### Exceptions

| Name | Summary |
|---|---|
| [ConvertException](-convert-exception/index.md) | 转换数据异常`class ConvertException : `[`NetException`](-net-exception/index.md) |
| [DownloadFileException](-download-file-exception/index.md) | 下载文件异常`class DownloadFileException : `[`NetException`](-net-exception/index.md) |
| [NetCancellationException](-net-cancellation-exception/index.md) | 取消网络任务的异常`class NetCancellationException : `[`CancellationException`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/CancellationException.html) |
| [NetConnectException](-net-connect-exception/index.md) | `class NetConnectException : `[`NetException`](-net-exception/index.md) |
| [NetException](-net-exception/index.md) | Net网络异常`open class NetException : `[`IOException`](https://docs.oracle.com/javase/6/docs/api/java/io/IOException.html) |
| [NetSocketTimeoutException](-net-socket-timeout-exception/index.md) | `class NetSocketTimeoutException : `[`NetException`](-net-exception/index.md) |
| [NetUnknownHostException](-net-unknown-host-exception/index.md) | `class NetUnknownHostException : `[`NetException`](-net-exception/index.md) |
| [NoCacheException](-no-cache-exception/index.md) | `class NoCacheException : `[`NetException`](-net-exception/index.md) |
| [RequestParamsException](-request-params-exception/index.md) | 400 - 499 客户端请求异常`class RequestParamsException : `[`NetException`](-net-exception/index.md) |
| [ResponseException](-response-exception/index.md) | 如果返回200但是返回数据不符合业务要求可以抛出该异常`class ResponseException : `[`NetException`](-net-exception/index.md) |
| [ServerResponseException](-server-response-exception/index.md) | = 500 服务器异常`class ServerResponseException : `[`NetException`](-net-exception/index.md) |
| [URLParseException](-u-r-l-parse-exception/index.md) | URL地址错误`open class URLParseException : `[`Exception`](https://docs.oracle.com/javase/6/docs/api/java/lang/Exception.html) |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [kotlinx.coroutines.CoroutineScope](kotlinx.coroutines.-coroutine-scope/index.md) |  |
