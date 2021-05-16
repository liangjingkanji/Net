[net](../../index.md) / [com.drake.net.interceptor](../index.md) / [LogRecordInterceptor](./index.md)

# LogRecordInterceptor

`class LogRecordInterceptor : Interceptor`

网络日志记录器
可以参考此拦截器为项目中其他网络请求库配置. 本拦截器属于标准的OkHttp拦截器适用于所有OkHttp拦截器内核的网络请求库

在正式环境下请禁用此日志记录器. 因为他会消耗少量网络速度

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | 网络日志记录器 可以参考此拦截器为项目中其他网络请求库配置. 本拦截器属于标准的OkHttp拦截器适用于所有OkHttp拦截器内核的网络请求库`LogRecordInterceptor(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, requestByteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1024 * 1024, responseByteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1024 * 1024 * 4)` |

### Properties

| Name | Summary |
|---|---|
| [enabled](enabled.md) | 是否启用日志输出`val enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [requestByteCount](request-byte-count.md) | 请求日志输出字节数`val requestByteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [responseByteCount](response-byte-count.md) | 响应日志输出字节数`val responseByteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

### Functions

| Name | Summary |
|---|---|
| [intercept](intercept.md) | `fun intercept(chain: Chain): Response` |
