[net](../index.md) / [com.drake.net.utils](./index.md)

## Package com.drake.net.utils

### Extensions for External Classes

| Name | Summary |
|---|---|
| [android.widget.EditText](android.widget.-edit-text/index.md) |  |
| [androidx.fragment.app.Fragment](androidx.fragment.app.-fragment/index.md) |  |
| [androidx.fragment.app.FragmentActivity](androidx.fragment.app.-fragment-activity/index.md) |  |
| [com.drake.brv.PageRefreshLayout](com.drake.brv.-page-refresh-layout/index.md) |  |
| [com.drake.statelayout.StateLayout](com.drake.statelayout.-state-layout/index.md) |  |
| [java.io.File](java.io.-file/index.md) |  |
| [kotlin.String](kotlin.-string/index.md) |  |
| [kotlinx.coroutines.CoroutineScope](kotlinx.coroutines.-coroutine-scope/index.md) |  |
| [kotlinx.coroutines.flow.Flow](kotlinx.coroutines.flow.-flow/index.md) |  |
| [okhttp3.OkHttpClient](okhttp3.-ok-http-client/index.md) |  |
| [okhttp3.OkHttpClient.Builder](okhttp3.-ok-http-client.-builder/index.md) |  |

### Functions

| Name | Summary |
|---|---|
| [lazyField](lazy-field.md) | 延迟初始化 线程安全 等效于[lazy](#), 但是可以获取委托字段属性`fun <T, V> T.lazyField(block: T.(`[`KProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)`<*>) -> V): `[`ReadWriteProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-write-property/index.html)`<T, V>` |
| [runMain](run-main.md) | 在主线程运行`fun runMain(block: () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [scope](scope.md) | 异步作用域`fun scope(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../com.drake.net.scope/-android-scope/index.md) |
| [scopeLife](scope-life.md) | `fun LifecycleOwner.scopeLife(lifeEvent: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../com.drake.net.scope/-android-scope/index.md) |
| [scopeNet](scope-net.md) | 该函数比[scope](kotlinx.coroutines.flow.-flow/scope.md)多了以下功能`fun scopeNet(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../com.drake.net.scope/-net-coroutine-scope/index.md) |
| [scopeNetLife](scope-net-life.md) | 该函数比scopeNet多了自动取消作用域功能`fun LifecycleOwner.scopeNetLife(lifeEvent: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../com.drake.net.scope/-net-coroutine-scope/index.md) |
| [withDefault](with-default.md) | 切换到默认调度器`suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T): T` |
| [withIO](with-i-o.md) | 切换到IO程调度器`suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T` |
| [withMain](with-main.md) | 切换到主线程调度器`suspend fun <T> withMain(block: suspend CoroutineScope.() -> T): T` |
| [withUnconfined](with-unconfined.md) | 切换到没有限制的调度器`suspend fun <T> withUnconfined(block: suspend CoroutineScope.() -> T): T` |
