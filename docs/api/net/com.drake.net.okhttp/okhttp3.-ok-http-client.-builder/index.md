[net](../../index.md) / [com.drake.net.okhttp](../index.md) / [okhttp3.OkHttpClient.Builder](./index.md)

### Extensions for okhttp3.OkHttpClient.Builder

| Name | Summary |
|---|---|
| [onDialog](on-dialog.md) | 全局加载对话框设置 设置在使用scopeDialog自动弹出的加载对话框`fun Builder.onDialog(block: `[`DialogCoroutineScope`](../../com.drake.net.scope/-dialog-coroutine-scope/index.md)`.(FragmentActivity) -> `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html)`): <ERROR CLASS>` |
| [onError](on-error.md) | 全局网络请求错误捕获`fun Builder.onError(block: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): <ERROR CLASS>` |
| [onStateError](on-state-error.md) | 全局缺省页错误捕获`fun Builder.onStateError(block: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`.(view: `[`View`](https://developer.android.com/reference/android/view/View.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): <ERROR CLASS>` |
| [setConverter](set-converter.md) | 转换器`fun Builder.setConverter(converter: `[`NetConverter`](../../com.drake.net.convert/-net-converter/index.md)`): <ERROR CLASS>` |
| [setHost](set-host.md) | 设置全局默认的Host, 在使用[com.drake.net.request.BaseRequest.setPath](../../com.drake.net.request/-base-request/set-path.md)的时候会成为默认的Host`fun Builder.setHost(host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): <ERROR CLASS>` |
| [setLog](set-log.md) | 开启日志`fun Builder.setLog(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): <ERROR CLASS>` |
| [setRequestInterceptor](set-request-interceptor.md) | 添加轻量级的请求拦截器, 可以在每次请求之前修改参数或者客户端配置 该拦截器不同于OkHttp的Interceptor无需处理请求动作`fun Builder.setRequestInterceptor(interceptor: `[`RequestInterceptor`](../../com.drake.net.interceptor/-request-interceptor/index.md)`): <ERROR CLASS>` |
| [setSSLCertificate](set-s-s-l-certificate.md) | `fun Builder.setSSLCertificate(trustManager: `[`X509TrustManager`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/X509TrustManager.html)`?, bksFile: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`? = null, password: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): <ERROR CLASS>`<br>`fun Builder.setSSLCertificate(vararg certificates: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, bksFile: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`? = null, password: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): <ERROR CLASS>` |
| [toNetOkhttp](to-net-okhttp.md) | Net拦截器代理OkHttp`fun Builder.toNetOkhttp(): <ERROR CLASS>` |
| [trustSSLCertificate](trust-s-s-l-certificate.md) | 信任所有证书`fun Builder.trustSSLCertificate(): <ERROR CLASS>` |
