如果你想要网络请求直接返回对应的Bean或者说POJO类你就要创建自定义的转换器, 来使用JSON解析框架处理你的数据(当然你可能是使用的protocol等其他数据格式)

在Net中转换器NetConverter负责数据解析的工作, 自定义转换器即实现NetConverter接口即可

> 你的业务可能需要请求参数加密或者拼接一串特殊含义的参数, 或者响应信息需要解密. 请不要尝试封装Post或者Get等请求函数(这是蠢材做法), 自定义拦截器和转换器可以应对任何项目需求.

- 数据解密
- 解析JSON
- 判断请求是否成功

<br>

## 创建转换器

一般情况我们可以直接继承JSONConvert(因为一般接口使用的是JSON返回)

=== "Gson"
    ```kotlin
    class GsonConvert : DefaultConvert(code = "code", message = "msg", success = "200") {
        val gson = GsonBuilder().serializeNulls().create()

        override fun <S> String.parseBody(succeed: Type): S? {
            return gson.fromJson(this, succeed)
        }
    }
    ```
=== "Moshi"
    ```kotlin
    class MoshiConvert : DefaultConvert(code = "code", message = "msg", success = "200") {
        val moshi = Moshi.Builder().build()

        override fun <S> String.parseBody(succeed: Type): S? {

            return moshi.adapter<S>(succeed).fromJson(this)
        }
    }
    ```
=== "FastJson"
    ```kotlin
    class FastJsonConvert : DefaultConvert(code = "code", message = "msg", success = "200") {

        override fun <S> String.parseBody(succeed: Type): S? {
            return JSON.parseObject(this, succeed)
        }
    }
    ```

- 请自己手动添加[Moshi](https://github.com/square/moshi)或者[Gson](https://github.com/google/gson)的依赖
- Moshi支持Kotlin构造参数默认值, 但是如果Json中字段为null依然会覆盖默认值为null
- 推荐使用官方的序列化框架: `kotlinx.Serialization`, 可以解析返回List, 支持Kotlin构造参数默认值, 可以非空覆盖

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
        setConverter(GsonConvert())
    }
    ```
=== "单例"
    ```kotlin hl_lines="3"
    scopeNetLife {
        tv_fragment.text = Get<String>("api"){
            converter = JsonConvert()
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