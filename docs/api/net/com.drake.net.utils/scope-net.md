[net](../index.md) / [com.drake.net.utils](index.md) / [scopeNet](./scope-net.md)

# scopeNet

`fun scopeNet(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../com.drake.net.scope/-net-coroutine-scope/index.md)

网络请求的异步作用域
自动显示错误信息吐司

该作用域生命周期跟随整个应用, 注意内存泄漏

