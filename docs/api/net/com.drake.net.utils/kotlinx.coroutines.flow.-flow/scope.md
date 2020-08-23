[net](../../index.md) / [com.drake.net.utils](../index.md) / [kotlinx.coroutines.flow.Flow](index.md) / [scope](./scope.md)

# scope

`inline fun <T> Flow<T>.scope(owner: LifecycleOwner? = null, event: Event = Lifecycle.Event.ON_DESTROY, crossinline action: suspend (value: T) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): CoroutineScope`