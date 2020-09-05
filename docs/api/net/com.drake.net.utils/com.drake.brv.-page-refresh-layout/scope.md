[net](../../index.md) / [com.drake.net.utils](../index.md) / [com.drake.brv.PageRefreshLayout](index.md) / [scope](./scope.md)

# scope

`fun PageRefreshLayout.scope(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`PageCoroutineScope`](../../com.drake.net.scope/-page-coroutine-scope/index.md)

PageRefreshLayout的异步作用域

1. 下拉刷新自动结束
2. 上拉加载自动结束
3. 捕获异常
4. 打印异常日志
5. 吐司部分异常[com.drake.net.NetConfig.onStateError](../../com.drake.net/-net-config/on-state-error.md)
6. 判断添加还是覆盖数据
7. 自动显示缺省页

布局被销毁或者界面关闭作用域被取消

