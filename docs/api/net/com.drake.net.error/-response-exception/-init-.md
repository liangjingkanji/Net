[net](../../index.md) / [com.drake.net.error](../index.md) / [ResponseException](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`ResponseException(code: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, request: Request)`

对应网络请求后台定义的错误信息

### Parameters

`msg` - 网络请求错误信息

`code` - 网络请求错误码

`tag` - 应对错误码判断为错时但是后端又返回了需要使用的数据(建议后端修改). 一般在Convert中设置数据