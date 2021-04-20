[net](../index.md) / [com.drake.net.scope](./index.md)

## Package com.drake.net.scope

### Types

| Name | Summary |
|---|---|
| [AndroidScope](-android-scope/index.md) | 异步协程作用域`open class AndroidScope : CoroutineScope, `[`Closeable`](https://docs.oracle.com/javase/6/docs/api/java/io/Closeable.html) |
| [DialogCoroutineScope](-dialog-coroutine-scope/index.md) | 自动加载对话框网络请求`class DialogCoroutineScope : `[`NetCoroutineScope`](-net-coroutine-scope/index.md)`, LifecycleObserver` |
| [NetCoroutineScope](-net-coroutine-scope/index.md) | 自动显示网络错误信息协程作用域`open class NetCoroutineScope : `[`AndroidScope`](-android-scope/index.md) |
| [PageCoroutineScope](-page-coroutine-scope/index.md) | `class PageCoroutineScope : `[`NetCoroutineScope`](-net-coroutine-scope/index.md) |
| [StateCoroutineScope](-state-coroutine-scope/index.md) | 缺省页作用域`class StateCoroutineScope : `[`NetCoroutineScope`](-net-coroutine-scope/index.md) |
