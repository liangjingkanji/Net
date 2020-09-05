[net](../../index.md) / [com.drake.net.utils](../index.md) / [androidx.fragment.app.Fragment](./index.md)

### Extensions for androidx.fragment.app.Fragment

| Name | Summary |
|---|---|
| [getSavedModel](get-saved-model.md) | 返回当前组件指定的SavedViewModel`fun <M : ViewModel> Fragment.getSavedModel(): M` |
| [scopeDialog](scope-dialog.md) | `fun Fragment.scopeDialog(dialog: `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html)`? = null, cancelable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md) |
| [scopeLife](scope-life.md) | `fun Fragment.scopeLife(lifeEvent: Event = Lifecycle.Event.ON_STOP, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../../com.drake.net.scope/-android-scope/index.md) |
| [scopeNetLife](scope-net-life.md) | Fragment应当在[Lifecycle.Event.ON_STOP](#)时就取消作用域, 避免[Fragment.onDestroyView](#)导致引用空视图`fun Fragment.scopeNetLife(lifeEvent: Event = Lifecycle.Event.ON_STOP, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md) |
