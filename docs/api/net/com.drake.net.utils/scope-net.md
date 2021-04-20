[net](../index.md) / [com.drake.net.utils](index.md) / [scopeNet](./scope-net.md)

# scopeNet

`fun scopeNet(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../com.drake.net.scope/-net-coroutine-scope/index.md)

该函数比[scope](kotlinx.coroutines.flow.-flow/scope.md)多了以下功能

* 在作用域内抛出异常时会被回调的[NetConfig.onError](#)函数中
* 自动显示错误信息吐司, 可以通过指定[NetConfig.onError](#)来取消或者增加自己的处理

该作用域生命周期跟随整个应用, 注意内存泄漏

