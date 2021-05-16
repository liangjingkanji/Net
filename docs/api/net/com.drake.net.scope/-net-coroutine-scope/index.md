[net](../../index.md) / [com.drake.net.scope](../index.md) / [NetCoroutineScope](./index.md)

# NetCoroutineScope

`open class NetCoroutineScope : `[`AndroidScope`](../-android-scope/index.md)

自动显示网络错误信息协程作用域

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | 自动显示网络错误信息协程作用域`NetCoroutineScope(lifecycleOwner: LifecycleOwner? = null, lifeEvent: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main)` |

### Properties

| Name | Summary |
|---|---|
| [animate](animate.md) | `var animate: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [error](error.md) | `var error: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCacheSucceed](is-cache-succeed.md) | `var isCacheSucceed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isReadCache](is-read-cache.md) | `var isReadCache: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [preview](preview.md) | `var preview: (suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)?` |

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `open fun cancel(cause: CancellationException?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [catch](catch.md) | `open fun catch(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [handleError](handle-error.md) | 错误处理`open fun handleError(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [launch](launch.md) | `open fun launch(block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](./index.md) |
| [preview](preview.md) | "预览"作用域 该函数一般用于缓存读取, 只在第一次启动作用域时回调 该函数在作用域[NetCoroutineScope.launch](launch.md)之前执行 函数内部所有的异常都不会被抛出, 也不会终止作用域执行`fun preview(ignore: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, animate: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../-android-scope/index.md) |
| [readCache](read-cache.md) | 读取缓存回调`open fun readCache(succeed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [start](start.md) | `open fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [DialogCoroutineScope](../-dialog-coroutine-scope/index.md) | 自动加载对话框网络请求`class DialogCoroutineScope : `[`NetCoroutineScope`](./index.md)`, LifecycleObserver` |
| [PageCoroutineScope](../-page-coroutine-scope/index.md) | `class PageCoroutineScope : `[`NetCoroutineScope`](./index.md) |
| [StateCoroutineScope](../-state-coroutine-scope/index.md) | 缺省页作用域`class StateCoroutineScope : `[`NetCoroutineScope`](./index.md) |
