[net](../../index.md) / [com.drake.net.scope](../index.md) / [DialogCoroutineScope](./index.md)

# DialogCoroutineScope

`class DialogCoroutineScope : `[`NetCoroutineScope`](../-net-coroutine-scope/index.md)`, LifecycleObserver`

自动加载对话框网络请求

开始: 显示对话框
错误: 提示错误信息, 关闭对话框
完全: 关闭对话框

### Parameters

`activity` - 对话框跟随生命周期的FragmentActivity

`dialog` - 不使用默认的加载对话框而指定对话框

`cancelable` - 是否允许用户取消对话框

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | 自动加载对话框网络请求`DialogCoroutineScope(activity: FragmentActivity, dialog: `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html)`? = null, cancelable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true)` |

### Properties

| Name | Summary |
|---|---|
| [activity](activity.md) | 对话框跟随生命周期的FragmentActivity`val activity: FragmentActivity` |
| [cancelable](cancelable.md) | 是否允许用户取消对话框`val cancelable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [dialog](dialog.md) | 不使用默认的加载对话框而指定对话框`var dialog: `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [dismiss](dismiss.md) | `fun dismiss(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [finally](finally.md) | `fun finally(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [handleError](handle-error.md) | 错误处理`fun handleError(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [readCache](read-cache.md) | 读取缓存回调`fun readCache(succeed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [start](start.md) | `fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
