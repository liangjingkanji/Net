[net](../index.md) / [com.drake.net.utils](index.md) / [scope](./scope.md)

# scope

`fun scope(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../com.drake.net.scope/-android-scope/index.md)

异步作用域

该作用域生命周期跟随整个应用, 注意内存泄漏

