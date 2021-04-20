[net](../../index.md) / [com.drake.net.request](../index.md) / [BaseRequest](index.md) / [setTag](./set-tag.md)

# setTag

`fun setTag(tag: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

将一个任意对象添加到Request对象中, 一般用于在拦截器或者转换器中被获取到标签, 针对某个请求的特殊业务逻辑
使用`Request.tag()`获取标签

`fun setTag(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tag: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

添加标签
使用`Request.tag(name)`得到指定标签

### Parameters

`name` - 标签名称

`tag` - 标签