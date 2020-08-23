[net](../../index.md) / [com.drake.net.utils](../index.md) / [androidx.fragment.app.Fragment](index.md) / [scopeNetLife](./scope-net-life.md)

# scopeNetLife

`fun Fragment.scopeNetLife(lifeEvent: Event = Lifecycle.Event.ON_STOP, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md)

Fragment应当在[Lifecycle.Event.ON_STOP](#)时就取消作用域, 避免[Fragment.onDestroyView](#)导致引用空视图

