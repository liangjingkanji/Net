[net](../../index.md) / [com.drake.net.scope](../index.md) / [AndroidScope](./index.md)

# AndroidScope

`open class AndroidScope : CoroutineScope`

异步协程作用域

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | 异步协程作用域`AndroidScope(lifecycleOwner: LifecycleOwner? = null, lifeEvent: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main)` |

### Properties

| Name | Summary |
|---|---|
| [catch](catch.md) | `var catch: (`[`AndroidScope`](./index.md)`.(`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)?` |
| [coroutineContext](coroutine-context.md) | `open val coroutineContext: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) |
| [dispatcher](dispatcher.md) | `val dispatcher: CoroutineDispatcher` |
| [finally](finally.md) | `var finally: (`[`AndroidScope`](./index.md)`.(`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)?` |
| [uid](uid.md) | `val uid: CoroutineExceptionHandler` |

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `open fun cancel(cause: CancellationException? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`open fun cancel(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cause: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [catch](catch.md) | `open fun catch(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>当作用域内发生异常时回调`open fun catch(block: `[`AndroidScope`](./index.md)`.(`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`AndroidScope`](./index.md) |
| [finally](finally.md) | `open fun finally(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>无论正常或者异常结束都将最终执行`open fun finally(block: `[`AndroidScope`](./index.md)`.(`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`AndroidScope`](./index.md) |
| [handleError](handle-error.md) | 错误处理`open fun handleError(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [launch](launch.md) | `open fun launch(block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](./index.md) |

### Inheritors

| Name | Summary |
|---|---|
| [NetCoroutineScope](../-net-coroutine-scope/index.md) | 自动显示网络错误信息协程作用域`open class NetCoroutineScope : `[`AndroidScope`](./index.md) |
