[net](../../index.md) / [com.drake.net.transform](../index.md) / [kotlinx.coroutines.Deferred](index.md) / [transform](./transform.md)

# transform

`fun <T, R> Deferred<T>.transform(block: (T) -> R): `[`DeferredTransform`](../-deferred-transform/index.md)`<T, R>`

可以将[Deferred](#)返回结果进行转换
[block](transform.md#com.drake.net.transform$transform(kotlinx.coroutines.Deferred((com.drake.net.transform.transform.T)), kotlin.Function1((com.drake.net.transform.transform.T, com.drake.net.transform.transform.R)))/block)在[Deferred](#)执行成功返回结果时执行

