
!!! question "请求参数"
    根据请求方式不同请求参数分为两类

    `UrlRequest`: GET, HEAD, OPTIONS, TRACE // Query(请求参数位于Url中) <br>
    `BodyRequest`: POST, DELETE, PUT, PATCH // Body(请求体为流)

使用示例

```kotlin
scopeNetLife {
    val userInfo = Post<UserInfoModel>(Api.LOGIN) {
        param("username", "用户名")
        param("password", "6f2961eb44b12123393fff7e449e50b9de2499c6")
    }.await()
}
```

|函数|描述|
|-|-|
|`param`| Url请求时为Query, Body请求时为表单/文件|
|`json`|JSON字符串|
|`setQuery/addQuery`|Url中的Query参数, 如果当为Url请求则该函数等效`param`|
|`setHeader/addHeader`|设置/添加请求头|

## JSON

三种参数类型上传JSON示例, 更多请阅读方法注释

=== "键值对"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100)
    
    scopeNetLife {
        tv.text = Post<String>(Api.PATH) {
            // 只支持基础类型的值, 如果值为对象或者包含对象的集合/数组会导致其值为null
            json("name" to name, "age" to age, "measurements" to measurements)
        }.await()
    }
    ```

=== "JSONObject"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100) // 只支持基础类型的值, 如果值为对象或者包含对象的集合/数组会导致其值为null

    scopeNetLife {
        tv.text = Post<String>(Api.PATH) {
            json(JSONObject().run {
                put("name", name)
                put("age", age)
                put("measurements", JSONArray(measurements))
            })
        }.await()
    }
    ```

=== "自定义RequestBody"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100)
    
    scopeNetLife {
        tv.text = Post<String>(Api.PATH) {
            body = CustomizerJSONBody(name, age, measurements)
        }.await()
    }
    ```

如果JSON需要全局参数

1. 自定义`RequestBody`添加全局参数
2. 使用请求拦截器来添加全局参数 [RequestInterceptor](interceptor.md#_1)

## 自定义扩展函数

由于`json()`不能传对象, 因为使用的`org.json.JSONObject`其不支持映射对象字段

但可创建扩展函数来使用射对象序列化框架来解析, 如下

=== "Gson"
    ```kotlin
    fun BodyRequest.gson(vararg body: Pair<String, Any?>) {
        this.body = Gson().toJson(body.toMap()).toRequestBody(MediaConst.JSON)
    }
    ```
=== "FastJson"
    ```kotlin
    fun BodyRequest.fastJson(vararg body: Pair<String, Any?>) {
        this.body = JSON.toJSON(body.toMap()).toRequestBody(MediaConst.JSON)
    }
    ```

使用

```kotlin
scopeNetLife {
    tv.text = Post<String>(Api.PATH) {
        gson("name" to name, "model" to Model()) // 参数支持Gson可解析的对象
        // fastJson("name" to name, "model" to Model()) // 使用FastJson
    }.await()
}
```

## 全局请求参数

使用`RequestInterceptor`请求拦截器添加全局参数/请求头, 更复杂请实现`Interceptor`

```kotlin
class GlobalHeaderInterceptor : RequestInterceptor {

    // 每次请求都会回调, 可以是动态参数
    override fun interceptor(request: BaseRequest) {
        request.setHeader("client", "Android")
        request.setHeader("token", UserConfig.token)
    }
}
```

```kotlin
NetConfig.initialize(Api.HOST, this) {
    setRequestInterceptor(GlobalHeaderInterceptor())
}
```

更多请求参数相关请阅读Api文档/函数列表