从Net3开始支持使用[kotlin-serialization](https://github.com/Kotlin/kotlinx.serialization)(简称KS). 可以指定任何类泛型解析成任何数据结构返回,
比如直接返回List/Map/Pair...


**Net是目前唯一支持kotlin-serialization转换器的网络请求库**

## kotlin-serialization 特点

1. kotlin官方出品, 推荐使用
2. kotlinx.serialization 是Kotlin上是最完美的序列化工具
3. 相对其他解析库他解决泛型擦除机制, 支持任何泛型, 可直接返回Map/List/Pair...
4. 多配置选项
5. 支持动态解析
6. 支持ProtoBuf/CBOR/JSON等数据序列化


## 配置转换器

这里使用Demo中的[SerializationConvert](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/convert/SerializationConvert.kt)作演示.
如果你业务有特殊需要可以复制Demo中的转换器代码稍加修改

=== "全局配置"
    ```kotlin
    initNet("http://google.com/") {
        setConverter(SerializationConvert())
        // ... 其他配置
    }
    ```
=== "单例配置"
    ```kotlin
    val userList = Get<List<UserModel>>("list-data") {
        converter = SerializationConvert() // 单例转换器, 此时会忽略全局转换器
    }.await()
    ```

## 使用

```kotlin
scopeNetLife {
    val userList = Get<List<UserModel>>("list-data") {
        // 该转换器直接解析JSON中的data字段, 而非返回的整个JSON字符串
        converter = SerializationConvert() // 单例转换器, 此时会忽略全局转换器
    }.await()

    tv_fragment.text = userList[0].name
}
```

> 具体解析返回的JSON中的某个字段请在转换器里面自定


