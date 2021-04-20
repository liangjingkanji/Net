[net](../index.md) / [androidx.lifecycle](./index.md)

## Package androidx.lifecycle

### Functions

| Name | Summary |
|---|---|
| [scopeLife](scope-life.md) | 在[ViewModel](#)被销毁时取消协程作用域`fun ViewModel.scopeLife(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../com.drake.net.scope/-android-scope/index.md) |
| [scopeNetLife](scope-net-life.md) | 在[ViewModel](#)被销毁时取消协程作用域以及其中的网络请求 具备网络错误全局处理功能, 其内部的网络请求会跟随其作用域的生命周期`fun ViewModel.scopeNetLife(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../com.drake.net.scope/-net-coroutine-scope/index.md) |
