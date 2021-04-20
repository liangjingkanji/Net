[net](../index.md) / [com.drake.net.utils](index.md) / [scopeNetLife](./scope-net-life.md)

# scopeNetLife

`fun LifecycleOwner.scopeNetLife(lifeEvent: Event = Lifecycle.Event.ON_DESTROY, dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../com.drake.net.scope/-net-coroutine-scope/index.md)

该函数比scopeNet多了自动取消作用域功能

该作用域生命周期跟随LifecycleOwner. 比如传入Activity会默认在[FragmentActivity.onDestroy](#)时取消网络请求.

### Parameters

`lifeEvent` - 指定LifecycleOwner处于生命周期下取消网络请求/作用域

`dispatcher` - 调度器, 默认运行在[Dispatchers.Main](#)即主线程下

**Receiver**
可传入FragmentActivity/AppCompatActivity, 或者其他的实现了LifecycleOwner的类

