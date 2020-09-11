[net](../../index.md) / [com.drake.net.utils](../index.md) / [kotlinx.coroutines.CoroutineScope](./index.md)

### Extensions for kotlinx.coroutines.CoroutineScope

| Name | Summary |
|---|---|
| [fastest](fastest.md) | 该函数将选择[deferredArray](fastest.md#com.drake.net.utils$fastest(kotlinx.coroutines.CoroutineScope, kotlin.Array((kotlinx.coroutines.Deferred((com.drake.net.utils.fastest.T)))))/deferredArray)中的Deferred执行[Deferred.await](#), 然后将返回最快的结果 执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常`suspend fun <T> CoroutineScope.fastest(vararg deferredArray: Deferred<T>): T`<br>`suspend fun <T, R> CoroutineScope.fastest(vararg deferredArray: `[`DeferredTransform`](../../com.drake.net.transform/-deferred-transform/index.md)`<T, R>): R`<br>该函数将选择[deferredList](fastest.md#com.drake.net.utils$fastest(kotlinx.coroutines.CoroutineScope, kotlin.collections.List((com.drake.net.transform.DeferredTransform((com.drake.net.utils.fastest.T, com.drake.net.utils.fastest.R)))))/deferredList)中的Deferred执行[Deferred.await](#), 然后将返回最快的结果 执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常`suspend fun <T, R> CoroutineScope.fastest(deferredList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DeferredTransform`](../../com.drake.net.transform/-deferred-transform/index.md)`<T, R>>): R` |
