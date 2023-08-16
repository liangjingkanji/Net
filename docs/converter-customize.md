Net自定义转换器可支持任何数据类型, 甚至`Bitmap`

!!! failure "泛型和转换器关系"
    1. 如果`Post<Model>`, 那么`NetConverter.onConvert`返回值必须为Model
    2. 如果`Post<Model?>`, 允许`NetConverter.onConvert`返回值为null
    3. 其他情况请抛出异常

```kotlin
scopeNetLife {
    val userList = Get<List<UserModel>>(Api.PATH) {
        converter = GsonConverter()
    }.await()
}
```

Net由于低耦合原则不自带任何序列化框架

## 设置转换器

=== "全局"
    ```kotlin hl_lines="2"
    NetConfig.initialize(Api.HOST, this) {
        setConverter(SerializationConverter())
    }
    ```
=== "单例"
    ```kotlin hl_lines="3"
    scopeNetLife {
       tv.text = Get<String>(Api.PATH){
            converter = SerializationConverter()
       }.await()
    }
    ```

## 常见转换器

实现[JSONConverter](https://github.com/liangjingkanji/Net/blob/master/net/src/main/java/com/drake/net/convert/JSONConvert.kt)的`parseBody`方法使用自定义序列化框架解析

| 序列化框架                                                   | 示例代码                                                       | 描述                 |
| ------------------------------------------------------------ | ------------------------------------------------------------ | -------------------- |
| [kotlin-serialization](https://github.com/Kotlin/kotlinx.serialization) | [SerializationConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/SerializationConverter.kt) | Kotlin官方序列化框架 |
| [kotlin-serialization](https://github.com/Kotlin/kotlinx.serialization) | [ProtobufConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/ProtobufConverter.kt) | Kotlin官方序列化框架 |
| [gson](https://github.com/google/gson)                       | [GsonConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/GsonConverter.kt) | 谷歌序列化框架       |
| [fastJson](https://github.com/alibaba/fastjson)              | [FastJsonConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/FastJsonConverter.kt) | 阿里巴巴序列化框架   |
| [moshi](https://github.com/square/moshi)                     | [MoshiConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/MoshiConverter.kt) | Square序列化框架     |

## 自定义转换器

实现`NetConverter`返回自定义请求结果

??? example "转换器实现非常简单"
    ```kotlin title="NetConverter.kt" linenums="1"
    interface NetConverter {

        @Throws(Throwable::class)
        fun <R> onConvert(succeed: Type, response: Response): R?

        companion object DEFAULT : NetConverter {
            /**
             * 返回结果应当等于泛型对象, 可空
             * @param succeed 请求要求返回的泛型类型
             * @param response 请求响应对象
             */
            override fun <R> onConvert(succeed: Type, response: Response): R? {
                return when {
                    succeed === String::class.java && response.isSuccessful -> response.body?.string() as R
                    succeed === ByteString::class.java && response.isSuccessful -> response.body?.byteString() as R
                    succeed is GenericArrayType && succeed.genericComponentType === Byte::class.java && response.isSuccessful -> response.body?.bytes() as R
                    succeed === File::class.java && response.isSuccessful -> response.file() as R
                    succeed === Response::class.java -> response as R
                    else -> throw ConvertException(response, "An exception occurred while converting the NetConverter.DEFAULT")
                }
            }
        }
    }
    ```

转换器中可以根据需加上解密数据, token失效跳转登录, 限制多端登录等逻辑

1. 日志信息输出, 请阅读[日志记录器](log-recorder.md)
2. 转换器中抛出异常被全局错误处理捕获, 请阅读[全局错误处理](error-handle.md)