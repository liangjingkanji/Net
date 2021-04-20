[net](../../index.md) / [com.drake.net.time](../index.md) / [Interval](./index.md)

# Interval

`class Interval : `[`Serializable`](https://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html)

轮循器

操作

1. 开启: 只有在闲置状态下才可以开始
2. 停止
3. 暂停
4. 继续
5. 重置: 重置不会导致轮循器停止
6. 开关: 开启|暂停切换
7. 生命周期

回调: 允许多次订阅同一个轮循器

1. 每个事件
2. 停止或者结束

### Parameters

`end` - 结束值

`period` - 计时器间隔

`unit` - 计时器单位

`initialDelay` - 第一次事件的间隔时间, 默认0

`start` - 开始值, 当[start](start.md)]比[end](end.md)值大, 且end不等于-1时, 即为倒计时, 反之正计时

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Interval(period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, unit: `[`TimeUnit`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/TimeUnit.html)`, initialDelay: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0)`<br>轮循器`Interval(end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, unit: `[`TimeUnit`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/TimeUnit.html)`, start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, initialDelay: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0)` |

### Properties

| Name | Summary |
|---|---|
| [count](count.md) | 轮循器的计数`var count: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [end](end.md) | 结束值`var end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [state](state.md) | 轮循器当前状态`var state: `[`IntervalStatus`](../-interval-status/index.md) |

### Functions

| Name | Summary |
|---|---|
| [finish](finish.md) | 轮循器完成时回调该函数`fun finish(block: (`[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Interval`](./index.md) |
| [life](life.md) | 绑定生命周期, 在指定生命周期发生时取消轮循器`fun life(lifecycleOwner: LifecycleOwner, lifeEvent: Event = Lifecycle.Event.ON_STOP): `[`Interval`](./index.md) |
| [pause](pause.md) | 暂停`fun pause(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [reset](reset.md) | 重置`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [resume](resume.md) | 继续 要求轮循器为暂停状态[IntervalStatus.STATE_PAUSE](../-interval-status/-s-t-a-t-e_-p-a-u-s-e.md), 否则无效`fun resume(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [start](start.md) | 开始`fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [stop](stop.md) | 停止`fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [subscribe](subscribe.md) | 订阅轮循器 每次轮循器计时都会调用该回调函数 轮循器完成时会同时触发回调[block](subscribe.md#com.drake.net.time.Interval$subscribe(kotlin.Function1((kotlin.Long, kotlin.Unit)))/block)和[finish](finish.md)`fun subscribe(block: (`[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Interval`](./index.md) |
| [switch](switch.md) | 切换轮循器开始或结束`fun switch(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
