
上一章节介绍如何序列化框架解析JSON, 而本章是介绍如何定义映射数据类

## JSON

解析接口返回的完整JSON

=== "JSON"
    ```json
    {
        "code":0,
        "msg":"请求成功",
        "data": {
            "name": "彭于晏",
            "age": 27,
            "height": 180
        }
    }
    ```

=== "数据类"
    ```kotlin
    data class UserModel (
        var code:Int,
        var msg:String,
        var data:Data,
    ) {
        data class Data(var name: String, var age: Int, var height: Int)
    }
    ```

=== "网络请求"
    ```kotlin
    scopeNetLife {
        val data = Get<UserModel>(Api.USER).await().data
    }
    ```

??? warning "以上设计不合理"
    正常情况下Http状态码200时只返回有效数据
    ```json
    {
        "name": "彭于晏",
        "age": 27,
        "height": 180
    }
    ```
    任何非正常流程返回200状态码, 例如400(错误请求)/401(认证失败)
    ```kotlin
    {
        "code":412302,
        "msg":"密码错误",
    }
    ```
    只要认为需要解析结构体情况下, 都应属于正常流程

## 剔除无效字段

以下演示仅解析`data`字段返回有效数据

此数据类只需要包含data值

```kotlin
data class UserModel(var name: String, var age: Int, var height: Int)
```

转换器只解析data字段

```kotlin
class GsonConvert : JSONConvert(code = "code", message = "msg", success = "200") {
    private val gson = GsonBuilder().serializeNulls().create()

    override fun <S> String.parseBody(succeed: Type): S? {
        val data = JSONObject(this).getString("data")
        return gson.fromJson(data, succeed)
    }
}
```

请求直接返回

```kotlin
scopeNetLife {
    val data = Get<UserModel>(Api.USER).await()
}
```

## 不规范数据

推荐在转换器中解析之前处理好数据

1. 字段值为`"null"`而不是`null`, 或者json在字符串中
   ```json
    {
      "data": "{ "title": "name" }"
      "msg": "null"
    }
   ```
    ```kotlin title="替换为规范内容"
    json = bodyString.replace("\"{", "{")
    json = bodyString.replace("}\"", "}")
    json = bodyString.replace("\"null\"", "null")
    ```

2. 服务器成功时不返回数据或者返回`null`
    ```kotlin
    if (response.body == null || bodyString == "null") {
        "{}".bodyString.parseBody<R>(succeed)
    }
    ```

3. 字段值为null, 使用 [kotlin-serialization](kotlin-serialization.md) 自动使用字段默认值
    ```kotlin
    {
        "msg": null
    }
    ```
4. 字段无引号或字段名为数字, 使用 [kotlin-serialization](kotlin-serialization.md) 可以禁用JSON规范限制
   ```json title="数字使用map解析"
    {
      "data": {
        23: 32
      }
    }
   ```
    ```kotlin hl_lines="3" title="禁用JSON规范限制"
    val jsonDecoder = Json {
        // ...
        isLenient = true
    }
    ```


## 泛型数据类

某些地区很多开发者习惯这么使用, 因为他们接口返回无关字段, 但是技术不够无法自定义转换器来简化取值

所以他们选择更复杂的方式: 使用泛型来简化

```kotlin
open class BaseResult<T> {
    var code: Int = 0
    var msg: String = ""
    var data: T? = null
}

class Result(var name: String) : BaseResult<Result>()
```

!!! quote "用加法解决问题的人，总有人愿意用乘法给你答案"