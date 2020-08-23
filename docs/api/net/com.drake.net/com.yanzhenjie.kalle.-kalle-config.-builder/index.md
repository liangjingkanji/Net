[net](../../index.md) / [com.drake.net](../index.md) / [com.yanzhenjie.kalle.KalleConfig.Builder](./index.md)

### Extensions for com.yanzhenjie.kalle.KalleConfig.Builder

| Name | Summary |
|---|---|
| [cacheEnabled](cache-enabled.md) | 开启缓存`fun Builder.cacheEnabled(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = NetConfig.app.cacheDir.absolutePath, password: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "cache"): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onDialog](on-dialog.md) | 设置使用DialogObserver默认弹出的加载对话框 默认使用系统自带的ProgressDialog`fun Builder.onDialog(block: `[`DialogCoroutineScope`](../../com.drake.net.scope/-dialog-coroutine-scope/index.md)`.(context: FragmentActivity) -> `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onError](on-error.md) | 该函数指定某些Observer的onError中的默认错误信息处理`fun Builder.onError(block: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onStateError](on-state-error.md) | 该函数指定某些Observer的onError中的默认错误信息处理`fun Builder.onStateError(block: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`.(view: `[`View`](https://developer.android.com/reference/android/view/View.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
