[net](../../index.md) / [com.drake.net.utils](../index.md) / [androidx.lifecycle.LifecycleOwner](index.md) / [scopeNetLife](./scope-net-life.md)

# scopeNetLife

`fun LifecycleOwner.scopeNetLife(lifeEvent: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md)