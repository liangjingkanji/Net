一般的解析过程是以下

1) 后端返回的JSON数据

```json
{
    "code":200,
    "msg":"错误信息",
    "data": {
        "name": "彭于晏"
    }
}
```

2) 创建数据模型

```kotlin
data class UserInfo (
    var code:Int,
    var msg:String,
    var data:Info,
) {
   data class Info (var name:String)
}
```

3) 发起网络请求

```kotlin
scopeNetLife {
    val data = Get<UserInfo>("/list").await().data
}
```

这样每次都要`await().data`才是你要的`data`对象. 有些人就想省略直接不写code和msg, 希望直接返回data.

这样的确可以, 但是面临一个问题, 部分后端开发可能让data直接为JSON数组.

例如这种格式

```json
{
    "code":200,
    "msg":"错误信息",
    "data": [
        { "name": "彭于晏" },
        { "name": "吴彦祖" },
        { "name": "金城武" }
    ]
}
```

由于Java的类型擦除机制, List的泛型在运行时将被擦除, 导致Gson或者FastJson等无法解析出正确的List数据

> 在Net的未来版本`3.0`中将支持直接返回List/Map/Pair等, 无需任何处理, 泛型是什么就返回什么

所以这样的代码将报错
```kotlin
scopeNetLife {
    val data = Get<List<Info>>("/list").await().data
}
```

这里我推荐的解决方式是, 针对会data会为JSON数组的情况, 我们可以直接使用String创建一个扩展函数

### 1) 定义解析函数

创建一个顶层函数(即在类之外的函数, 直接存在kt文件中)
```kotlin
inline fun <reified T> String.toJsonArray(): MutableList<T> {
    return JSON.parseArray(this, T::class.java)
}
```

`JSON.parseArray`这是FastJson的函数, 如果你使用的是Gson可以使用

```kotlin
inline fun <reified T> String.toJsonArray(): MutableList<T> {
    return Gson().fromJson(this, TypeToken.getParameterized(List::class.java, T::class.java).type)
}
```

### 2) 创建数据类

```kotlin
data class Info (var name:String)
```

### 3) 使用

```kotlin
scopeNetLife {
    val listData = Post<String>("/list").await().toJsonArray<Info>()
    listData[0]
}
```
<br>

> 这样的优点是扩展方便, 整体清晰. 毕竟不是每个接口的data都可能是JSON数组, 而且这样还支持List嵌套(JSON数组嵌套)<br>
  该扩展函数太简单我就不加入到框架中了, 大家复制粘贴下就OK了