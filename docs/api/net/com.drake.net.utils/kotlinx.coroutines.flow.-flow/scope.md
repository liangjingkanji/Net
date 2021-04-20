[net](../../index.md) / [com.drake.net.utils](../index.md) / [kotlinx.coroutines.flow.Flow](index.md) / [scope](./scope.md)

# scope

`inline fun <T> Flow<T>.scope(owner: LifecycleOwner? = null, event: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main, crossinline action: suspend CoroutineScope.(value: T) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../../com.drake.net.scope/-android-scope/index.md)

Flow直接创建作用域

### Parameters

`owner` - 跟随的生命周期组件

`event` - 销毁时机

`dispatcher` - 指定调度器