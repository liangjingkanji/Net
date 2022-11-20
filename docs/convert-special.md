
## 解析完整Json

一般的解析过程是以下

1. 后端返回的JSON数据

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

2. 创建数据模型

```kotlin
data class UserModel (
    var code:Int,
    var msg:String,
    var data:Data,
) {
    data class Data(var name: String, var age: Int, var height: Int)
}
```

3. 发起网络请求

```kotlin
scopeNetLife {
    val data = Get<UserModel>("api").await().data
}
```

## 解析Json中的字段

这样每次都要`await().data`才是你要的`data`对象. 有些人就想省略直接不写code和msg, 希望直接返回data. 那么在转换器里面就只解析data字段即可

简化数据对象

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

使用简化的数据对象作为泛型

```kotlin
scopeNetLife {
    val data = Get<UserModel>("api").await()
}
```

## 解析Json数组

在Net中可以直接解析List等嵌套泛型数据, 解析List和普通对象没有区别

```kotlin
scopeNetLife {
    tvFragment.text = Get<List<UserModel>>("list") {
        converter = GsonConverter() // 单例转换器, 一般情况下是定义一个全局转换器
    }.await()[0].name
}
```

## 解析泛型数据类

这种方式在Retrofit中经常被使用到, 因为有些人认为code/msg也要使用. 实际上一般非成功错误码(例如200或者0)算业务定义错误应当在转换器抛出异常, 然后在错误处理回调中取获取错误码/信息
    ```kotlin
    // 数据对象的基类
    open class BaseResult<T> {
        var code: Int = 0
        var msg: String = ""
        var data: T? = null
    }

    class Result(var name: String) : BaseResult<Result>()
    ```

如果你成功错误码要求定义多个都算网络请求成功, 也是可以的并且不需要写泛型这么麻烦, Net转换器可以实现无论加不加`code/msg`都能正常解析返回

```kotlin
@kotlinx.serialization.Serializable
class Result(var data: String = "数据", var msg: String = "", var code:Int = 0)
```

```kotlin
@kotlinx.serialization.Serializable
class Result(var data: String = "数据")
```

查看源码`SerializationConverter`可以看到转换器内进行了回退解析策略, 当截取`data`解析失败后会完整解析整个Json

```kotlin hl_lines="15"
code in 200..299 -> { // 请求成功
    val bodyString = response.body?.string() ?: return null
    val kType = response.request.kType
        ?: throw ConvertException(response, "Request does not contain KType")
    return try {
        val json = JSONObject(bodyString) // 获取JSON中后端定义的错误码和错误信息
        val srvCode = json.getString(this.code)
        if (srvCode == success) { // 对比后端自定义错误码
            json.getString("data").parseBody<R>(kType)
        } else { // 错误码匹配失败, 开始写入错误异常
            val errorMessage = json.optString(message, NetConfig.app.getString(com.drake.net.R.string.no_error_message))
            throw ResponseException(response, errorMessage, tag = srvCode) // 将业务错误码作为tag传递
        }
    } catch (e: JSONException) { // 固定格式JSON分析失败直接解析JSON
        bodyString.parseBody<R>(kType)
    }
}
```