[net](../../index.md) / [com.drake.net.okhttp](../index.md) / [okhttp3.OkHttpClient.Builder](index.md) / [setRequestInterceptor](./set-request-interceptor.md)

# setRequestInterceptor

`fun Builder.setRequestInterceptor(interceptor: `[`RequestInterceptor`](../../com.drake.net.interceptor/-request-interceptor/index.md)`): <ERROR CLASS>`

添加轻量级的请求拦截器, 可以在每次请求之前修改参数或者客户端配置
该拦截器不同于OkHttp的Interceptor无需处理请求动作

