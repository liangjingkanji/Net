一般的JSON数据都是
```json
{
    code:"200",
    msg:"错误信息",
    data: {
        name: "value"
    }
}
```

我们可以直接复制这个JSON来创建数据模型
```kotlin
scopeNetLife {
    val data = Get<UserInfo>("/list").await().data
}
```

但是这样每次都要`.data`才是你要的真实数据. 有些人就想省略直接不写code和msg, 希望直接返回data. 这样的确可以, 但是面临一个问题, 部分后端开发可能让data直接为JSON数组.
由于Java的类型擦除机制, List的泛型在运行时将被擦除, 导致Gson或者FastJson等无法解析出正确的List模型

所以这样的代码将报错
```kotlin
scopeNetLife {
    val data = Get<List<Data>>("/list").await().data
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

### 2) 使用

```kotlin
scope {
    val listData = Post<String>("/list").await().toJsonArray<Data>()
    listData[0]
}
```
<br>

> 这样的优点是扩展方便, 整体清晰. 毕竟不是每个接口的data都可能是JSON数组, 而且这样还支持List嵌套(JSON数组嵌套)<br>
  该扩展函数太简单我就不加入到框架中了, 大家复制粘贴下就OK了