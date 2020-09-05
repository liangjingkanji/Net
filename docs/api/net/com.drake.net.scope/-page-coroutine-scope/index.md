[net](../../index.md) / [com.drake.net.scope](../index.md) / [PageCoroutineScope](./index.md)

# PageCoroutineScope

`class PageCoroutineScope : `[`NetCoroutineScope`](../-net-coroutine-scope/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PageCoroutineScope(page: PageRefreshLayout, dispatcher: CoroutineDispatcher = Dispatchers.Main)` |

### Properties

| Name | Summary |
|---|---|
| [index](--index--.md) | `val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [page](page.md) | `val page: PageRefreshLayout` |

### Functions

| Name | Summary |
|---|---|
| [catch](catch.md) | `fun catch(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [finally](finally.md) | `fun finally(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [handleError](handle-error.md) | 错误处理`fun handleError(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [readCache](read-cache.md) | 读取缓存回调`fun readCache(succeed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [start](start.md) | `fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
