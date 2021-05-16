[net](../../index.md) / [com.drake.net.convert](../index.md) / [JSONConvert](./index.md)

# JSONConvert

`abstract class JSONConvert : `[`NetConverter`](../-net-converter/index.md)

常见的JSON转换器实现, 如果不满意继承实现自定义的业务逻辑

### Parameters

`success` - 后端定义为成功状态的错误码值

`code` - 错误码在JSON中的字段名

`message` - 错误信息在JSON中的字段名

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | 常见的JSON转换器实现, 如果不满意继承实现自定义的业务逻辑`JSONConvert(success: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "0", code: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "code", message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "msg")` |

### Properties

| Name | Summary |
|---|---|
| [code](code.md) | 错误码在JSON中的字段名`val code: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [message](message.md) | 错误信息在JSON中的字段名`val message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [success](success.md) | 后端定义为成功状态的错误码值`val success: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [onConvert](on-convert.md) | `open fun <R> onConvert(succeed: `[`Type`](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Type.html)`, response: Response): R?` |
| [parseBody](parse-body.md) | 反序列化JSON`abstract fun <S> `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`.parseBody(succeed: `[`Type`](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Type.html)`): S?` |
