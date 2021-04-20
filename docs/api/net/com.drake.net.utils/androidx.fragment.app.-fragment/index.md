[net](../../index.md) / [com.drake.net.utils](../index.md) / [androidx.fragment.app.Fragment](./index.md)

### Extensions for androidx.fragment.app.Fragment

| Name | Summary |
|---|---|
| [scopeDialog](scope-dialog.md) | `fun Fragment.scopeDialog(dialog: `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html)`? = null, cancelable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md) |
| [scopeLife](scope-life.md) | `fun Fragment.scopeLife(lifeEvent: Event = Lifecycle.Event.ON_STOP, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../../com.drake.net.scope/-android-scope/index.md) |
| [scopeNetLife](scope-net-life.md) | 和上述函数功能相同, 只是接受者为Fragment`fun Fragment.scopeNetLife(lifeEvent: Event = Lifecycle.Event.ON_STOP, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md) |
