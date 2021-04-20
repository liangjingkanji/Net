[net](../../index.md) / [com.drake.net.utils](../index.md) / [kotlinx.coroutines.flow.Flow](index.md) / [listen](./listen.md)

# listen

`inline fun <T> Flow<T>.listen(owner: LifecycleOwner? = null, event: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main, crossinline action: suspend CoroutineScope.(value: T) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../../com.drake.net.scope/-android-scope/index.md)

收集Flow结果并过滤重复结果

