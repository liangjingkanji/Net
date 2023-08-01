1. 从Net3开始支持使用 [Kotlin-Serialization](https://github.com/Kotlin/kotlinx.serialization) (以下简称ks)
2. 更多了解请阅读: [Kotlin最强解析库 - kotlin-serialization](https://juejin.cn/post/6963676982651387935)

## 特点

- 官方库
- 动态数据类型解析
- 自定义序列化器
- 支持ProtoBuf/JSON等数据结构序列化
- 非空覆盖(即JSON字段为null则使用变量默认值)
- 启用宽松模式, JSON和数据类结构无需一致
- 解析任何类型(Map/List/Pair...)

!!! Failure "强制注解"
    ks的数据类都要求使用注解`@Serializable`(除非自定义解析), 父类和子类都需要



!!! Success "生成默认值"
    使用[插件生成数据Model](model-generate.md), 支持自动生成默认值和注解

    生成默认值可避免后端返回异常数据导致解析崩溃, 以及反复编写判空代码

## 依赖


Project build.gradle


```kotlin
classpath "org.jetbrains.kotlin:kotlin-serialization:$kotlin_version"
// 和Kotlin插件同一个版本号即可
```

Model build.gradle

```kotlin
apply plugin: "kotlin-kapt"
apply plugin: 'kotlinx-serialization'
implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
```

## 配置转换器

这里使用示例代码中 [SerializationConvert](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/converter/SerializationConverter.kt) 作为演示

=== "全局配置"
    ```kotlin
    NetConfig.initialize(Api.HOST, this) {
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

