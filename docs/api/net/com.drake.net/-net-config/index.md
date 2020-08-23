[net](../../index.md) / [com.drake.net](../index.md) / [NetConfig](./index.md)

# NetConfig

`object NetConfig`

Net的全局配置

### Properties

| Name | Summary |
|---|---|
| [app](app.md) | 全局上下文, 一般执行[initNet](../android.app.-application/init-net.md)即可, 无需手动赋值`lateinit var app: `[`Application`](https://developer.android.com/reference/android/app/Application.html) |
| [host](host.md) | 域名或者ip(baseUrl)`lateinit var host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [onDialog](on-dialog.md) | 全局加载框`var onDialog: `[`DialogCoroutineScope`](../../com.drake.net.scope/-dialog-coroutine-scope/index.md)`.(FragmentActivity) -> `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html) |
| [onError](on-error.md) | 全局错误处理`var onError: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onStateError](on-state-error.md) | 全局缺省页错误处理`var onStateError: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`.(view: `[`View`](https://developer.android.com/reference/android/view/View.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
