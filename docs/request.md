Net中关于请求的类只有两个类和他们共同的抽象父类

```kotlin
BaseRequest
    |- UrlRequest
    |- BodyRequest
```


根据请求方法不同使用的Request也不同

```kotlin
GET, HEAD, OPTIONS, TRACE, // Url request
POST, DELETE, PUT, PATCH // Body request
```

代码示例

```kotlin
scopeNetLife {
    Get<String>("api") {
        // this 即为 UrlRequest
    }.await()

    Post<String>("api") {
        // this 即为 BodyRequest
    }.await()
}
```

## 请求参数

```kotlin
scopeNetLife { // 创建作用域
    // 这个大括号内就属于作用域内部
    val data = Get<String>("http://www.baidu.com/"){
        param("u_name", "drake")
        param("pwd", "123456")
    }.await() // 发起GET请求并返回`String`类型数据
}
```

|请求函数|描述|
|-|-|
|`param`|支持基础类型/文件/RequestBody/Part|
|`json`|请求参数为JSONObject/JsonArray/String|
|`setQuery`|Url参数, 如果当前请求为Url请求则该函数等效于`param`函数|

如果没有添加文件/流那么就是通过BodyRequest内部的`FormBody`发起请求. 反之就是通过`MultipartBody`发起请求.

> 当然你可以完全自定义Body来请求, 譬如以下的Json请求


## JSON请求

这里提供三种创建Json请求的示例代码. 酌情选用

=== "JSON键值对(推荐)"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100)

    scopeNetLife {
        tvFragment.text = Post<String>("api") {
            json("name" to name, "age" to age, "measurements" to measurements) // 同时也支持Map集合
        }.await()
    }
    ```

=== "JSONObject"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100)

    scopeNetLife {
        tvFragment.text = Post<String>("api") {
            json(JSONObject().run {
                put("name", name)
                put("age", age)
                put("measurements", JSONArray(measurements))
            })
        }.await()
    }
    ```

=== "自定义的body"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100)

    scopeNetLife {
        tvFragment.text = Post<String>("api") {
            body = MyJsonBody(name, age, measurements)
        }.await()
    }
    ```

对于某些可能JSON请求参数存在固定值:

1. 可以考虑继承RequestBody来扩展出自己的新的Body对象, 然后赋值给`body`字段
2. 添加请求拦截器[RequestInterceptor](/interceptor/#_1)


## 请求函数

关于全部的请求配置选项推荐阅读函数文档或者阅读源码. Net提供清晰的函数结构浏览方便直接阅读源码

<img src="https://i.imgur.com/j9H4c1y.png" width="400"/>

