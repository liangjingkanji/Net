从Net3开始支持使用[kotlin-serialization](https://github.com/Kotlin/kotlinx.serialization)(简称KS). 我愿称他为最强序列化库

<br>
**Net完美支持kotlin-serialization创建转换器**

## kotlin-serialization 特点

1. kotlin官方出品, 推荐使用
2. kotlinx.serialization 是Kotlin上是最完美的序列化工具
3. 相对其他解析库他解决泛型擦除机制, 支持任何泛型, 可直接返回Map/List/Pair...
4. 配置选项多
5. 支持动态解析
6. 自定义序列化器
7. 支持ProtoBuf/CBOR/JSON等其他数据结构序列化
8. 可以配置成后端如果返回null则使用数据模型字段的默认值(非空覆盖)
9. 启用宽松模式, 允许配置成后端和前端数据模型字段是否严苛匹配对应

> 注意Ks的数据模型类都要求使用注解`@Serializable`. <br>
> 一般开发中都是使用[插件生成数据模型](model-generate.md), 所以这并不会增加工作量. 即使手写也只是一个注解, 但是可以带来默认值支持和更安全的数据解析

## 配置转换器

这里使用Demo中的[SerializationConvert](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/converter/SerializationConverter.kt)作演示.
如果你业务有特殊需要可以复制Demo中的转换器代码稍加修改

=== "全局配置"
    ```kotlin
    NetConfig.init("http://google.com/") {
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


