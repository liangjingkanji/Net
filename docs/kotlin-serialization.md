从Net3开始支持使用[kotlin-serialization](https://github.com/Kotlin/kotlinx.serialization)(以下简称ks)

中文使用文档请阅读: [Kotlin最强解析库 - kotlin-serialization](https://juejin.cn/post/6963676982651387935)

<br>
**Net完美支持kotlin-serialization创建转换器**

## kotlin-serialization 特点

- kotlin官方发行
- 可配置性强
- 支持动态解析
- 自定义序列化器
- 支持ProtoBuf/CBOR/JSON等其他数据结构序列化
- 非空覆盖(即返回的Json字段为null则使用数据类默认值)
- 启用宽松模式, 允许Json和数据类字段匹配不一致
- 相对其他解析库他解决泛型擦除机制, 支持任何泛型, 可直接返回Map/List/Pair...

> 注意ks的数据模型类都要求使用注解`@Serializable`. <br>
> 一般开发中都是使用[插件生成数据模型](model-generate.md), 所以这并不会增加工作量. 即使手写也只是一个注解, 但是可以带来默认值支持和更安全的数据解析

## 配置转换器

这里使用Demo中的[SerializationConvert](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/converter/SerializationConverter.kt)作演示.
如果你业务有特殊需要可以复制Demo中的转换器代码稍加修改

=== "全局配置"
    ```kotlin
    NetConfig.initialize("https://github.com/liangjingkanji/Net/") {
        setConverter(SerializationConvert())
        // ... 其他配置
    }
    ```
=== "单例配置"
    ```kotlin
    val userList = Get<List<UserModel>>("list") {
        converter = SerializationConvert() // 单例转换器, 此时会忽略全局转换器
    }.await()
    ```

## 使用

```kotlin
scopeNetLife {
    // 这里后端直接返回的Json数组
    val userList = Get<List<UserModel>>("list") {
        converter = SerializationConvert()
    }.await()

    tvFragment.text = userList[0].name
}
```

```kotlin
@Serializable
data class UserModel(var name: String, var age: Int, var height: Int)
```

> 具体解析返回的JSON中的某个字段请在转换器里面自定 <br>
如果想详细了解KS, 请阅读文章: [Kotlin最强解析库 - kotlin-serialization](https://juejin.cn/post/6963676982651387935)

## 非空覆盖

在开发中遇到不规范的后端接口数据时为了避免空指针等数据异常则需要使用`非空覆盖`
即返回的Json字段为null或者不存在则使用数据类默认值. 避免导致的空指针问题或者数据异常.

Json配置
```kotlin
val jsonDecoder = Json {
    ignoreUnknownKeys = true // JSON和数据模型字段可以不匹配
    coerceInputValues = true // 如果JSON字段是Null则使用默认值
}
```

数据类使用默认值
```kotlin
@Serializable
data class Data(var name:String = "", var age:Int = 0)
```

手动写默认值太麻烦, 推荐使用插件生成默认值

<img src="https://i.loli.net/2021/11/19/YahlbxO9dWf1PN5.png" width="600"/>

插件具体配置使用请查看: [数据模型生成插件](model-generate.md)

