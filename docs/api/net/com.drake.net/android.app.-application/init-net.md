[net](../../index.md) / [com.drake.net](../index.md) / [android.app.Application](index.md) / [initNet](./init-net.md)

# initNet

`fun `[`Application`](https://developer.android.com/reference/android/app/Application.html)`.initNet(host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, config: Builder.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

初始化框架

### Parameters

`host` - 请求url的主机名

`config` -

进行配置网络请求



如果想要自动解析数据模型请配置转换器, 可以继承或者参考默认转换器

**See Also**

[com.drake.net.convert.DefaultConvert](../../com.drake.net.convert/-default-convert/index.md)

