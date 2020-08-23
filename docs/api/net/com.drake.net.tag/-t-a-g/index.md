[net](../../index.md) / [com.drake.net.tag](../index.md) / [TAG](./index.md)

# TAG

`abstract class TAG`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `TAG()` |

### Properties

| Name | Summary |
|---|---|
| [list](list.md) | `var list: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`TAG`](./index.md)`>?` |

### Functions

| Name | Summary |
|---|---|
| [contains](contains.md) | `operator fun contains(tag: `[`TAG`](./index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [get](get.md) | `operator fun get(key: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`TAG`](./index.md)`>): `[`TAG`](./index.md)`?` |
| [minus](minus.md) | `operator fun minus(tag: `[`TAG`](./index.md)`): `[`TAG`](./index.md) |
| [plus](plus.md) | `operator fun plus(tag: `[`TAG`](./index.md)`): `[`TAG`](./index.md) |

### Inheritors

| Name | Summary |
|---|---|
| [REQUEST](../-r-e-q-u-e-s-t.md) | 请求参数打印标签`object REQUEST : `[`TAG`](./index.md) |
| [RESPONSE](../-r-e-s-p-o-n-s-e.md) | 响应体打印标签`object RESPONSE : `[`TAG`](./index.md) |
