[net](../index.md) / [com.drake.net.utils](index.md) / [lazyField](./lazy-field.md)

# lazyField

`fun <T, V> T.lazyField(block: T.(`[`KProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)`<*>) -> V): `[`ReadWriteProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-write-property/index.html)`<T, V>`

延迟初始化
线程安全
等效于[lazy](#), 但是可以获取委托字段属性

