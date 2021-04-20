[net](../../index.md) / [com.drake.net.utils](../index.md) / [kotlinx.coroutines.flow.Flow](./index.md)

### Extensions for kotlinx.coroutines.flow.Flow

| Name | Summary |
|---|---|
| [listen](listen.md) | 收集Flow结果并过滤重复结果`fun <T> Flow<T>.listen(owner: LifecycleOwner? = null, event: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main, action: suspend CoroutineScope.(value: T) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../../com.drake.net.scope/-android-scope/index.md) |
| [scope](scope.md) | Flow直接创建作用域`fun <T> Flow<T>.scope(owner: LifecycleOwner? = null, event: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main, action: suspend CoroutineScope.(value: T) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../../com.drake.net.scope/-android-scope/index.md) |
