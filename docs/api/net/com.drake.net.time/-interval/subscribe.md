[net](../../index.md) / [com.drake.net.time](../index.md) / [Interval](index.md) / [subscribe](./subscribe.md)

# subscribe

`fun subscribe(block: (`[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Interval`](index.md)

订阅轮循器
每次轮循器计时都会调用该回调函数
轮循器完成时会同时触发回调[block](subscribe.md#com.drake.net.time.Interval$subscribe(kotlin.Function1((kotlin.Long, kotlin.Unit)))/block)和[finish](finish.md)

