[net](../../index.md) / [com.drake.net.scope](../index.md) / [AndroidScope](index.md) / [finally](./finally.md)

# finally

`protected var finally: (`[`AndroidScope`](index.md)`.(`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)?``protected open fun finally(e: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

### Parameters

`e` - 如果发生异常导致作用域执行完毕, 则该参数为该异常对象, 正常结束则为null`open fun finally(block: `[`AndroidScope`](index.md)`.(`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`AndroidScope`](index.md)

无论正常或者异常结束都将最终执行

