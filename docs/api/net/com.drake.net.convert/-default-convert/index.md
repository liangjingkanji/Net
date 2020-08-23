[net](../../index.md) / [com.drake.net.convert](../index.md) / [DefaultConvert](./index.md)

# DefaultConvert

`abstract class DefaultConvert : Converter`

默认的转换器实现, 如果不满足需求建议将该文件复制到项目中修改

### Parameters

`success` - 后端定义为成功状态的错误码值

`code` - 错误码在JSON中的字段名

`message` - 错误信息在JSON中的字段名

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | 默认的转换器实现, 如果不满足需求建议将该文件复制到项目中修改`DefaultConvert(success: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "0", code: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "code", message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "msg")` |

### Properties

| Name | Summary |
|---|---|
| [code](code.md) | 错误码在JSON中的字段名`val code: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [message](message.md) | 错误信息在JSON中的字段名`val message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [success](success.md) | 后端定义为成功状态的错误码值`val success: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [convert](convert.md) | `open fun <S, F> convert(succeed: `[`Type`](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Type.html)`, failed: `[`Type`](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Type.html)`, request: Request, response: Response, result: Result<S, F>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [parseBody](parse-body.md) | 解析字符串数据 一般用于解析JSON`abstract fun <S> `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`.parseBody(succeed: `[`Type`](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Type.html)`): S?` |
