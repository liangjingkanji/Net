[net](../../index.md) / [com.drake.net.scope](../index.md) / [NetCoroutineScope](index.md) / [preview](./preview.md)

# preview

`protected var preview: (suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)?``fun preview(ignore: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, animate: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`AndroidScope`](../-android-scope/index.md)

"预览"作用域
该函数一般用于缓存读取, 只在第一次启动作用域时回调
该函数在作用域[NetCoroutineScope.launch](launch.md)之前执行
函数内部所有的异常都不会被抛出, 也不会终止作用域执行

### Parameters

`ignore` - 是否在缓存读取成功但网络请求错误时吐司错误信息

`animate` - 是否在缓存成功后依然显示加载动画

`block` - 该作用域内的所有异常都算缓存读取失败, 不会吐司和打印任何错误