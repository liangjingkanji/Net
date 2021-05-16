[net](../../index.md) / [com.drake.net.convert](../index.md) / [NetConverter](./index.md)

# NetConverter

`interface NetConverter`

### Functions

| Name | Summary |
|---|---|
| [onConvert](on-convert.md) | `abstract fun <R> onConvert(succeed: `[`Type`](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Type.html)`, response: Response): R?` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [DEFAULT](-d-e-f-a-u-l-t.md) | 返回数据为字符串内容`val DEFAULT: `[`NetConverter`](./index.md) |

### Inheritors

| Name | Summary |
|---|---|
| [JSONConvert](../-j-s-o-n-convert/index.md) | 常见的JSON转换器实现, 如果不满意继承实现自定义的业务逻辑`abstract class JSONConvert : `[`NetConverter`](./index.md) |
