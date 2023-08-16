Net自定义转换器可支持任何数据类型, 甚至`Bitmap`

!!! failure "泛型和转换器关系"
    1. 如果`Post<Model>`, 那么`NetConverter.onConvert`返回值必须为Model
    2. 如果`Post<Model?>`, 允许`NetConverter.onConvert`返回值为null
    3. 任何错误请在转换器中直接抛出异常

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

实现[JSONConverter](https://github.com/liangjingkanji/Net/blob/master/net/src/main/java/com/drake/net/convert/JSONConvert.kt)接口快速实现JSON解析, 或直接复制以下转换器示例

| 序列化框架                                                   | 示例代码                                                       | 描述                 |
| ------------------------------------------------------------ | ------------------------------------------------------------ | -------------------- |
| [kotlin-serialization](https://github.com/Kotlin/kotlinx.serialization) | [SerializationConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/SerializationConverter.kt) | Kotlin官方序列化框架 |
| [kotlin-serialization](https://github.com/Kotlin/kotlinx.serialization) | [ProtobufConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/ProtobufConverter.kt) | Kotlin官方序列化框架 |
| [gson](https://github.com/google/gson)                       | [GsonConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/GsonConverter.kt) | 谷歌序列化框架       |
| [fastJson](https://github.com/alibaba/fastjson)              | [FastJsonConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/FastJsonConverter.kt) | 阿里巴巴序列化框架   |
| [moshi](https://github.com/square/moshi)                     | [MoshiConverter](https://github.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/converter/MoshiConverter.kt) | Square序列化框架     |

## 自定义转换器

转换器原理非常简单, 实现`NetConverter`接口返回一个对象(等于请求泛型)


???+ example "建议保留默认支持的类型"
    ```kotlin hl_lines="5"
    class CustomizeConverter: NetConverter {

        override fun <R> onConvert(succeed: Type, response: Response): R? {
            try {
                return NetConverter.onConvert<R>(succeed, response)
            } catch (e: ConvertException) {
                // ... 仅自定义不支持的类型
                return 任何对象 as R
            }
        }
    }
    ```

转换器中可以根据错误码抛出自定义异常

??? example "转换器异常链"
    ```kotlin
    // 非CancellationException/NetException及其子类的上抛ConvertException
    try {
        return request.converter().onConvert<R>(type, this) as R
    } catch (e: CancellationException) {
        throw e
    } catch (e: NetException) {
        throw e
    } catch (e: Throwable) {
        throw ConvertException(this, cause = e)
    }
    ```


1. [日志记录](log-recorder.md)建议使用拦截器
2. 转换器中抛出异常被[全局错误处理](error-handle.md)捕获