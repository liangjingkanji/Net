首先我们知道Net可以通过泛型指定返回的数据类型

```kotlin
scopeNetLife {
    tv_fragment.text = Post<Model>("api").await()
}
```

如果是你解析的JSON对象中包含集合, 这种情况完全无需考虑. 直接解析JSON数组字段-data 才会存在以上的问题

```json
{
    "code":200,
    "msg":"错误信息",
    "data": [
        { "name": "彭于晏" },
        { "name": "彭于晏" },
        { "name": "彭于晏" }
    ]
}
```

如果后端直接返回JSON数组

```json
[
    { "name": "彭于晏" },
    { "name": "彭于晏" },
    { "name": "彭于晏" }
]
```

你们可能会这么写

```kotlin
scopeNetLife {
    tv_fragment.text = Post<List<Model>>("api").await()
}
```

> 但是由于Java的泛型擦除机制, 这么会发生异常无法转换成正确的数据返回


解决办法

1. 泛型使用Array替换List
2. 泛型使用String, 然后写扩展函数转换下

## 使用Array

```kotlin
scopeNetLife {
    val arr = Post<Array<Model>>("api").await()
    arr.toMutableList() // 如果你就是要求集合, 可以使用函数将数组转换成集合
}
```

## 使用扩展函数

我们可以直接使用String创建一个扩展函数


### 1. 定义解析函数

创建一个顶层函数(即在类之外的函数, 直接存在kt文件中)

FastJSON解析库演示

```kotlin
inline fun <reified T> String.toJsonArray(): MutableList<T> {
    return JSON.parseArray(this, T::class.java)
}
```

Gson解析库演示

```kotlin
inline fun <reified T> String.toJsonArray(): MutableList<T> {
    return Gson().fromJson(this, TypeToken.getParameterized(List::class.java, T::class.java).type)
}
```

### 2. 使用

```kotlin
scopeNetLife {
    val listData = Post<String>("/list").await().toJsonArray<Info>()
}
```

> 该扩展函数太简单我就不加入到框架中了, 大家复制粘贴下就OK了