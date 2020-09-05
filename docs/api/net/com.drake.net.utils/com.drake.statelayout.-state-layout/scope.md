[net](../../index.md) / [com.drake.net.utils](../index.md) / [com.drake.statelayout.StateLayout](index.md) / [scope](./scope.md)

# scope

`fun StateLayout.scope(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`NetCoroutineScope`](../../com.drake.net.scope/-net-coroutine-scope/index.md)

自动处理缺省页的异步作用域
作用域开始执行时显示加载中缺省页
作用域正常结束时显示成功缺省页
作用域抛出异常时显示错误缺省页
并且自动吐司错误信息, 可配置 [com.drake.net.NetConfig.onStateError](../../com.drake.net/-net-config/on-state-error.md)
自动打印异常日志

**Receiver**

当前视图会被缺省页包裹



布局被销毁或者界面关闭作用域被取消

