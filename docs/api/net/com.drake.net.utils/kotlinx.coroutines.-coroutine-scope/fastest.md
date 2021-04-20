[net](../../index.md) / [com.drake.net.utils](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [fastest](./fastest.md)

# fastest

`suspend fun <T> CoroutineScope.fastest(listDeferred: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<Deferred<T>>, group: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null): T`

该函数将选择[listDeferred](fastest.md#com.drake.net.utils$fastest(kotlinx.coroutines.CoroutineScope, kotlin.collections.List((kotlinx.coroutines.Deferred((com.drake.net.utils.fastest.T)))), kotlin.Any)/listDeferred)中的Deferred执行[Deferred.await](#), 然后将返回最快的结果
执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常

### Parameters

`group` - 指定该值将在成功返回结果后取消掉对应uid的网络请求

`listDeferred` - 一系列并发任务`@JvmName("fastestTransform") suspend fun <T, R> CoroutineScope.fastest(listDeferred: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DeferredTransform`](../../com.drake.net.transform/-deferred-transform/index.md)`<T, R>>?, group: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = null): R`

该函数将选择[listDeferred](fastest.md#com.drake.net.utils$fastest(kotlinx.coroutines.CoroutineScope, kotlin.collections.List((com.drake.net.transform.DeferredTransform((com.drake.net.utils.fastest.T, com.drake.net.utils.fastest.R)))), kotlin.Any)/listDeferred)中的Deferred执行[Deferred.await](#), 然后将返回最快的结果
执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常

### Parameters

`group` - 指定该值将在成功返回结果后取消掉对应uid的网络请求

`listDeferred` - 一系列并发任务

**See Also**

[DeferredTransform](../../com.drake.net.transform/-deferred-transform/index.md)

