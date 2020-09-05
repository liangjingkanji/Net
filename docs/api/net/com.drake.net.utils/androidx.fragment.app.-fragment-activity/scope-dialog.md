[net](../../index.md) / [com.drake.net.utils](../index.md) / [androidx.fragment.app.FragmentActivity](index.md) / [scopeDialog](./scope-dialog.md)

# scopeDialog

`fun FragmentActivity.scopeDialog(dialog: `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html)`? = null, cancelable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md)

作用域开始时自动显示加载对话框, 结束时自动关闭加载对话框
可以设置全局对话框 [com.drake.net.NetConfig.onDialog](../../com.drake.net/-net-config/on-dialog.md)

### Parameters

`dialog` -

仅该作用域使用的对话框



对话框被取消或者界面关闭作用域被取消

