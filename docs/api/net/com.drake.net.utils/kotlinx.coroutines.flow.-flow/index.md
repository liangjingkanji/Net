[net](../../index.md) / [com.drake.net.utils](../index.md) / [kotlinx.coroutines.flow.Flow](./index.md)

### Extensions for kotlinx.coroutines.flow.Flow

| Name | Summary |
|---|---|
| [listen](listen.md) | 监听数据库`fun <T> Flow<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.listen(lifecycleOwner: LifecycleOwner? = null, lifeEvent: Event = Lifecycle.Event.ON_DESTROY, block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [scope](scope.md) | `fun <T> Flow<T>.scope(owner: LifecycleOwner? = null, event: Event = Lifecycle.Event.ON_DESTROY, action: suspend (value: T) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): CoroutineScope` |
