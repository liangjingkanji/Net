[net](../../index.md) / [com.drake.net.time](../index.md) / [Interval](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Interval(period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, unit: `[`TimeUnit`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/TimeUnit.html)`, initialDelay: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0)``Interval(end: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, unit: `[`TimeUnit`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/TimeUnit.html)`, start: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, initialDelay: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0)`

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