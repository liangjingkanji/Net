[net](../../index.md) / [com.drake.net.interceptor](../index.md) / [RetryInterceptor](./index.md)

# RetryInterceptor

`class RetryInterceptor : Interceptor`

重试次数拦截器

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | 重试次数拦截器`RetryInterceptor(retryCount: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 3)` |

### Properties

| Name | Summary |
|---|---|
| [retryCount](retry-count.md) | 重试次数`val retryCount: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [intercept](intercept.md) | `fun intercept(chain: Chain): Response` |
