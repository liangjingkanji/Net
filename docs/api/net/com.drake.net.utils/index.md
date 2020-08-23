[net](../index.md) / [com.drake.net.utils](./index.md)

## Package com.drake.net.utils

### Types

| Name | Summary |
|---|---|
| [SavedViewModel](-saved-view-model/index.md) | `open class SavedViewModel : ViewModel` |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [android.database.Cursor](android.database.-cursor/index.md) |  |
| [androidx.fragment.app.Fragment](androidx.fragment.app.-fragment/index.md) |  |
| [androidx.fragment.app.FragmentActivity](androidx.fragment.app.-fragment-activity/index.md) |  |
| [androidx.lifecycle.LifecycleOwner](androidx.lifecycle.-lifecycle-owner/index.md) |  |
| [androidx.lifecycle.ViewModelStoreOwner](androidx.lifecycle.-view-model-store-owner/index.md) |  |
| [com.drake.brv.PageRefreshLayout](com.drake.brv.-page-refresh-layout/index.md) |  |
| [com.drake.statelayout.StateLayout](com.drake.statelayout.-state-layout/index.md) |  |
| [kotlinx.coroutines.flow.Flow](kotlinx.coroutines.flow.-flow/index.md) |  |

### Functions

| Name | Summary |
|---|---|
| [runMain](run-main.md) | 在主线程运行`fun runMain(block: () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [scope](scope.md) | 异步作用域`fun scope(block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../com.drake.net.scope/-android-scope/index.md) |
| [scopeNet](scope-net.md) | 网络请求的异步作用域 自动显示错误信息吐司`fun scopeNet(block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../com.drake.net.scope/-net-coroutine-scope/index.md) |
| [withDefault](with-default.md) | 切换到默认调度器`suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T): T` |
| [withIO](with-i-o.md) | 切换到IO程调度器`suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T` |
| [withMain](with-main.md) | 切换到主线程调度器`suspend fun <T> withMain(block: suspend CoroutineScope.() -> T): T` |
| [withUnconfined](with-unconfined.md) | 切换到没有限制的调度器`suspend fun <T> withUnconfined(block: suspend CoroutineScope.() -> T): T` |
