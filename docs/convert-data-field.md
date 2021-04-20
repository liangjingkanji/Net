
## 转换完整的JSON

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

}
```

3) 发起网络请求

```kotlin
scopeNetLife {
    val data = Get<UserInfo>("/list").await().data
}
```

## 仅转换Data字段

这样每次都要`await().data`才是你要的`data`对象. 有些人就想省略直接不写code和msg, 希望直接返回data. 那么在转换器里面就只解析data字段即可

简化数据对象

```kotlin
data class Info (var name:String)
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
    val data = Get<Info>("/list").await().data
}
```

## 使用泛型数据对象

这种方式在Retrofit中经常被使用到, 可能某些人比较习惯

数据对象

```kotlin
// 数据对象的基类
open class BaseResult<T> {
    var code: Int = 0
    var msg: String = ""
    var data: T? = null
}

class Result(var request_method: String) : BaseResult<Result>()
```

使用泛型

```kotlin
scopeNetLife {
    val data = Get<Result>("api").await().request_method
}
```

