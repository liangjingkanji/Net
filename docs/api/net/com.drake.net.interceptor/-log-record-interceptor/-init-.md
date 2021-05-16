[net](../../index.md) / [com.drake.net.interceptor](../index.md) / [LogRecordInterceptor](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`LogRecordInterceptor(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, requestByteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1024 * 1024, responseByteCount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1024 * 1024 * 4)`

网络日志记录器
可以参考此拦截器为项目中其他网络请求库配置. 本拦截器属于标准的OkHttp拦截器适用于所有OkHttp拦截器内核的网络请求库

在正式环境下请禁用此日志记录器. 因为他会消耗少量网络速度

