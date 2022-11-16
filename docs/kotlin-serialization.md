1. 从Net3开始支持使用[kotlin-serialization](https://github.com/Kotlin/kotlinx.serialization)(以下简称ks)

1. 更多使用教程请阅读: [Kotlin最强解析库 - kotlin-serialization](https://juejin.cn/post/6963676982651387935)

<br>
## kotlin-serialization 特点

- kotlin官方库
- 动态数据类型解析
- 自定义序列化器
- 支持ProtoBuf/JSON等数据结构序列化
- 非空覆盖(即返回的Json字段为null则使用数据类默认值)
- 启用宽松模式, Json和数据类字段无需一致
- 解析任何类型(Map/List/Pair...)

> ks的数据模型类都要求使用注解`@Serializable`(除非自定义解析过程), 父类和子类都需要使用 <br>
> 一般开发中都是使用[插件生成数据模型](model-generate.md), 所以这并不会增加工作量. 即使手写也只是一个注解, 但是可以带来默认值支持和更安全的数据解析

## 依赖


项目 build.gradle


```kotlin
classpath "org.jetbrains.kotlin:kotlin-serialization:$kotlin_version"
// 和Kotlin插件同一个版本号即可
```

module build.gradle

```kotlin
apply plugin: "kotlin-kapt"
apply plugin: 'kotlinx-serialization'
implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
```

## 配置转换器

这里使用Demo中的[SerializationConvert](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/converter/SerializationConverter.kt)作演示.
如果你业务有特殊需要可以复制Demo中的转换器代码稍加修改

=== "全局配置"
    ```kotlin
    NetConfig.initialize("https://github.com/liangjingkanji/Net/", this) {
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

> 具体解析返回的JSON中的某个字段请在转换器里面自定, 其注意如果存在父类, 父类和子类都需要使用`@Serializable`注解修饰 <br>
如果想详细了解KS, 请阅读文章: [Kotlin最强解析库 - kotlin-serialization](https://juejin.cn/post/6963676982651387935)

## 常用配置

以下为反序列化Json常用配置

```kotlin
val jsonDecoder = Json {
    ignoreUnknownKeys = true // 数据类可以不用声明Json的所有字段
    coerceInputValues = true // 如果Json字段是Null则使用数据类字段默认值
}
```

数据类使用默认值
```kotlin
@Serializable
data class Data(var name:String = "", var age:Int = 0)
```
> 建议为数据类所有字段设置默认值, 避免后端数据缺失导致解析异常, 也减少频繁判空操作

### 启用默认值

当`coerceInputValues = true`时, json字段为null数据类字段为非空类型情况下采用字段默认值, 没有默认值请`explicitNulls = false`则赋值为null

同时当出现未知的枚举类型也会使用默认值



### 字段缺失

通过`explicitNulls`来配置字段缺时处理方式

- 反序列化时, 如果数据类缺失字段, 使用`ignoreUnknownKeys = true`就会自动使用默认值, 没有默认值请`explicitNulls = false`赋值为null
- 序列化时, 数据类如果字段为null也会被赋值到json中, `explicitNulls = false`可以忽略掉


### 自动生成

手动写默认值太麻烦, 推荐使用插件生成默认值

<img src="https://i.loli.net/2021/11/19/YahlbxO9dWf1PN5.png" width="600"/>

插件具体配置使用请查看: [数据模型生成插件](model-generate.md)

