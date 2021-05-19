网络请求响应被封装到Response对象中, 普通接口我们需要读取映射到数据模型对象中

Net使用泛型指定返回数据类型

```kotlin
scopeNetLife {
    val userList = Get<List<UserModel>>("list-data") {
        converter = SerializationConverter()
    }.await()
}
```

## 默认支持类型

默认使用的是: [NetConverter.DEFAULT](https://github.com/liangjingkanji/Net/blob/master/net/src/main/java/com/drake/net/convert/NetConverter.kt)

| 函数 | 描述 |
|-|-|
| String | 字符串 |
| ByteArray | 字节数组 |
| ByteString | 内部定义的一种字符串对象 |
| Response | 最基础的响应 |
| File | 文件对象, 这种情况其实应当称为`下载文件` |


> 你的业务可能需要参数加密解密或者拼接参数, 请不要尝试封装Post或者Get等请求函数(这不是一个好主意), 自定义拦截器和转换器可以应对任何项目需求


<br>

Demo截图预览

<img src="https://i.loli.net/2021/05/18/yUBmka6AjKsVleP.png" width="300"/>

<br>

## 创建转换器

一般业务我们可以直接继承[JSONConverter](https://github.com/liangjingkanji/Net/blob/master/net/src/main/java/com/drake/net/convert/JSONConvert.kt)
使用自己的JSON解析器解析数据, 自定义需求可以直接实现[NetConverter](https://github.com/liangjingkanji/Net/blob/master/net/src/main/java/com/drake/net/convert/NetConverter.kt)

=== "Kotlin-Serialization"

    [Kotlin-Serialization GitHub](https://github.com/Kotlin/kotlinx.serialization)

    由于代码量比较重可以查看源码或者Demo: [SerializationConverter](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/converter/SerializationConverter.kt)

    SerializationConverter和JSONConverter代码差不多

=== "Gson"

    [GSON  GitHub](https://github.com/google/gson)
    ```kotlin
    class GsonConvert : JSONConvert(code = "code", message = "msg", success = "200") {
        val gson = GsonBuilder().serializeNulls().create()

        override fun <S> String.parseBody(succeed: Type): S? {
            return gson.fromJson(this, succeed)
        }
    }
    ```
=== "Moshi"

    [Moshi GitHub](https://github.com/square/moshi)
    ```kotlin
    class MoshiConvert : JSONConvert(code = "code", message = "msg", success = "200") {
        val moshi = Moshi.Builder().build()

        override fun <S> String.parseBody(succeed: Type): S? {
            return moshi.adapter<S>(succeed).fromJson(this)
        }
    }
    ```
=== "FastJson"

    [FastJson GitHub](https://github.com/alibaba/fastjson)

    ```kotlin
    class FastJsonConvert : JSONConvert(code = "code", message = "msg", success = "200") {

        override fun <S> String.parseBody(succeed: Type): S? {
            return JSON.parseObject(this, succeed)
        }
    }
    ```

1. 使用对应转换器添加对应依赖
2. 推荐使用 `kotlinx.Serialization`, 其可解析[任何泛型](kotlin-serialization.md)
3. 请阅读Demo源码

| 转换器参数 | 描述 |
|-|-|
| code | 即后端定义的`成功码`字段名 |
| message | 即后端定义的`错误消息`字段名 |
| success | 即`成功码`的值等于指定时才算网络请求成功 |

<br>
> 注意解析器(Gson或者Moshi)的对象记得定义为类成员, 这样可以不会导致每次解析都要创建一个新的对象, 减少内存消耗

<br>

## 设置转换器
转换器分为全局和单例, 单例可以覆盖全局的转换器, 一般情况下设置一个全局即可

=== "全局"
    ```kotlin hl_lines="2"
    initNet("http://github.com/") {
        setConverter(SerializationConverter())
    }
    ```
=== "单例"
    ```kotlin hl_lines="3"
    scopeNetLife {
        tv_fragment.text = Get<String>("api"){
            converter = SerializationConverter()
        }.await()
    }
    ```


## 自定义转换器

通过实现`NetConverter`接口可以编写自己的逻辑网络请求返回的数据, `NetConvert.DEFAULT`为默认的转换器支持返回File/String/Response等

```kotlin
val DEFAULT = object : NetConverter {

    override fun <R> onConvert(
        succeed: Type,
        response: Response
    ): R? {
        return when (succeed) {
            String::class.java -> response.body?.string() as R
            ByteString::class.java -> response.body?.byteString() as R
            ByteArray::class.java -> response.body?.bytes() as R
            Response::class.java -> response as R
            File::class.java -> response.file() as R
            else -> throw ConvertException(
                response,
                "The default converter does not support this type"
            )
        }
    }
}
```

框架中自带一个`JSONConverter`可以作为参考或者直接使用. 其可以转换JSON数据.

源码如下
```kotlin
abstract class JSONConvert(
    val success: String = "0",
    val code: String = "code",
    val message: String = "msg"
) : NetConverter {

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        try {
            return NetConverter.DEFAULT.onConvert<R>(succeed, response)
        } catch (e: ConvertException) {
            val body = response.body?.string() ?: return null
            val code = response.code
            when {
                code in 200..299 -> { // 请求成功
                    if (succeed === String::class.java) return body as R
                    val jsonObject = JSONObject(body) // 获取JSON中后端定义的错误码和错误信息
                    if (jsonObject.getString(this.code) == success) { // 对比后端自定义错误码
                        return body.parseBody(succeed)
                    } else { // 错误码匹配失败, 开始写入错误异常
                        throw ResponseException(response, jsonObject.getString(message))
                    }
                }
                code in 400..499 -> throw RequestParamsException(response) // 请求参数错误
                code >= 500 -> throw ServerResponseException(response) // 服务器异常错误
                else -> throw ConvertException(response)
            }
        }
    }

    /**
     * 反序列化JSON
     *
     * @param succeed JSON对象的类型
     * @receiver 原始字符串
     */
    abstract fun <S> String.parseBody(succeed: Type): S?
}
```

JSONConvert的核心逻辑

1. 判断服务器错误码
1. 判断后端自定义错误码
1. 如果判断错误则创建一个包含错误信息的异常
1. 如果都判断成功则开始解析数据

根据需要你可以在这里加上常见的日志打印, 解密数据, 跳转登录界面等逻辑

<br>

> 转换器允许返回null, 如果你有任何认为不支持或者需要中断请求的操作可以在转换器中抛出任何异常, 推荐你的自定义异常继承`NetException`