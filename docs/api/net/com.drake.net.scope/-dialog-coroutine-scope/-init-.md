[net](../../index.md) / [com.drake.net.scope](../index.md) / [DialogCoroutineScope](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`DialogCoroutineScope(activity: FragmentActivity, dialog: `[`Dialog`](https://developer.android.com/reference/android/app/Dialog.html)`? = null, cancelable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true)`

自动加载对话框网络请求

开始: 显示对话框
错误: 提示错误信息, 关闭对话框
完全: 关闭对话框

### Parameters

`activity` - 对话框跟随生命周期的FragmentActivity

`dialog` - 不使用默认的加载对话框而指定对话框

`cancelable` - 是否允许用户取消对话框