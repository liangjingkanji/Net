[net](../../index.md) / [com.drake.net.utils](../index.md) / [androidx.lifecycle.LifecycleOwner](./index.md)

### Extensions for androidx.lifecycle.LifecycleOwner

| Name | Summary |
|---|---|
| [observe](observe.md) | 快速创建LiveData的观察者`fun <M> LifecycleOwner.observe(liveData: LiveData<M>?, block: M?.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [scopeLife](scope-life.md) | `fun LifecycleOwner.scopeLife(lifeEvent: Event = Lifecycle.Event.ON_DESTROY, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../../com.drake.net.scope/-android-scope/index.md) |
| [scopeNetLife](scope-net-life.md) | `fun LifecycleOwner.scopeNetLife(lifeEvent: Event = Lifecycle.Event.ON_DESTROY, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md) |
