[net](../../index.md) / [com.drake.net](../index.md) / [NetConfig](./index.md)

# NetConfig

`object NetConfig`

Net的全局配置

### Properties

| Name | Summary |
|---|---|
| [app](app.md) | 全局上下文, 一般执行[initNet](../init-net.md)即可, 无需手动赋值`lateinit var app: `[`Application`](https://developer.android.com/reference/android/app/Application.html) |
| [converter](converter.md) | `var converter: `[`NetConverter`](../../com.drake.net.convert/-net-converter/index.md) |
| [host](host.md) | 全局的域名或者ip(baseUrl)`var host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [logEnabled](log-enabled.md) | `var logEnabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [netCalls](net-calls.md) | `var netCalls: `[`ConcurrentLinkedQueue`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html)`<`[`WeakReference`](https://docs.oracle.com/javase/6/docs/api/java/lang/ref/WeakReference.html)`<Call>>` |
| [okHttpClient](ok-http-client.md) | `lateinit var okHttpClient: OkHttpClient` |
| [onDialog](on-dialog.md) | 全局加载框`var onDialog: `[`DialogCoroutineScope`](../../com.drake.net.scope/-dialog-coroutine-scope/index.md)`.(FragmentActivity) -> `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html) |
| [onError](on-error.md) | 全局错误处理`var onError: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onStateError](on-state-error.md) | 全局缺省页错误处理`var onStateError: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`.(view: `[`View`](https://developer.android.com/reference/android/view/View.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [requestInterceptor](request-interceptor.md) | `var requestInterceptor: `[`RequestInterceptor`](../../com.drake.net.interceptor/-request-interceptor/index.md)`?` |
