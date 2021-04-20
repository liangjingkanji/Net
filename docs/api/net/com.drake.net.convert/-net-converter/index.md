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
| [JSONConvert](../-j-s-o-n-convert/index.md) | 默认的转换器实现, 如果不满足需求建议将该文件复制到项目中修改`abstract class JSONConvert : `[`NetConverter`](./index.md) |
