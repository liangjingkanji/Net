[net](../index.md) / [androidx.lifecycle](index.md) / [scopeLife](./scope-life.md)

# scopeLife

`fun ViewModel.scopeLife(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../com.drake.net.scope/-android-scope/index.md)

在[ViewModel](#)被销毁时取消协程作用域

