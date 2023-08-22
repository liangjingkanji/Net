
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

## JSON请求

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

## 自定义请求函数

比如由于`json()`使用的`org.json.JSONObject`不支持序列化对象

可以创建扩展函数来使用自定义序列化框架来解决, 如下

=== "Gson"
    ```kotlin
    fun BodyRequest.gson(vararg body: Pair<String, Any?>) {
        this.body = Gson().toJson(body.toMap()).toRequestBody(MediaConst.JSON)
    }
    ```
=== "FastJson"
    ```kotlin
    fun BodyRequest.fastJson(obj: Any) {
        this.body = JSON.toJSON(obj).toRequestBody(MediaConst.JSON)
    }
    ```

使用

```kotlin
scopeNetLife {
    tv.text = Post<String>(Api.PATH) {
        gson("name" to name, "data" to Data())
        // fastJson(data)
    }.await()
}
```

## 自定义请求体

要求实现`RequestBody`接口, 可参考Net上传Uri/File的实现源码

??? example "FileRequestBody"
    ```kotlin
    fun File.toRequestBody(contentType: MediaType? = null): RequestBody {
        val fileMediaType = contentType ?: mediaType()
        return object : RequestBody() {

            // 文件类型
            override fun contentType(): MediaType? {
                return fileMediaType
            }

            // 文件长度, 不确定返回-1
            override fun contentLength() = length()

            // 写入数据
            override fun writeTo(sink: BufferedSink) {
                source().use { source ->
                    sink.writeAll(source)
                }
            }
        }
    }
    ```

??? example "UriRequestBody"
    ```kotlin
    fun Uri.toRequestBody(): RequestBody {
        val document = DocumentFile.fromSingleUri(NetConfig.app, this)
        val contentResolver = NetConfig.app.contentResolver
        val contentLength = document?.length() ?: -1L
        val contentType = mediaType()
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return contentType
            }

            override fun contentLength() = contentLength

            override fun writeTo(sink: BufferedSink) {
                contentResolver.openInputStream(this@toRequestBody)?.use {
                    sink.writeAll(it.source())
                }
            }
        }
    }
    ```

使用
```kotlin hl_lines="4"
scopeNetLife {
    tv.text = Post<String>(Api.PATH) {
        // 完全自定义请求体, 会忽略其他请求参数
        body = CustomizerRequestBody()
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