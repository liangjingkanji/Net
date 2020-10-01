在Net中转换器([Convert](https://github.com/liangjingkanji/Net/blob/master/kalle/src/main/java/com/yanzhenjie/kalle/simple/Converter.kt))负责数据解析的工作, 自定义转换器即实现Convert接口即可

!!! note
    你的业务可能需要请求参数加密或者拼接一串特殊含义的参数, 或者响应信息需要解密. 请不要尝试封装Post或者Get等请求函数(这是蠢材做法), 自定义拦截器和转换器可以应对任何项目需求.

- 数据解密
- 解析JSON
- 判断请求是否成功

<br>

## 创建转换器

一般情况我们可以直接继承[DefaultConvert](api/net/com.drake.net.convert/-default-convert/index.md)

=== "Gson"
    ```kotlin
    class JsonConvert : DefaultConvert(code = "code", message = "msg", success = "200") {
        override fun <S> String.parseBody(succeed: Type): S? {
            return Gson().fromJson(this, succeed)
        }
    }
    ```
=== "Moshi"
    ```kotlin
    class JsonConvert : DefaultConvert(code = "code", message = "msg", success = "200") {
        override fun <S> String.parseBody(succeed: Type): S? {
            return Moshi.Builder().build().adapter<S>(succeed).fromJson(this)
        }
    }
    ```

- 请自己手动添加[Moshi](https://github.com/square/moshi)或者[Gson](https://github.com/google/gson)的依赖
- Moshi属于Kotlin上解析Json我比较推荐的一个解析库, 支持Kotlin默认值(Gson不支持)

<br>

## 设置转换器
转换器分为全局和单例, 单例可以覆盖全局的转换器, 一般情况下设置一个全局即可

=== "全局"
    ```kotlin hl_lines="2"
    initNet("http://182.92.97.186/") {
        converter(JsonConvert())
    }
    ```
=== "单例"
    ```kotlin hl_lines="3"
    scopeNetLife {
        tv_fragment.text = Get<String>("api"){
            convert(JsonConvert())
        }.await()
    }
    ```


## 自定义转换器

[DefaultConvert](https://github.com/liangjingkanji/Net/blob/master/net/src/main/java/com/drake/net/convert/DefaultConvert.kt) 实际上也只是实现Convert接口的一个抽象类
!!! note "自定义转换器"
    当你对DefaultConvert有更多需求或者功能扩展的话推荐参考DefaultConvert来实现Convert接口(直接复制DefaultConvert文件修改也行)
源码如下
```kotlin
abstract class DefaultConvert(
    val success: String = "0",
    val code: String = "code",
    val message: String = "msg"
) : Converter {

    override fun <S, F> convert(
        succeed: Type,
        failed: Type,
        request: Request,
        response: Response,
        result: Result<S, F>
    ) {

        val body = response.body().string()
        val code = response.code()

        when {
            code in 200..299 -> {  // 服务器错误码为200-299时为成功
                val jsonObject = JSONObject(body)
                if (jsonObject.getString(this.code) == success) { // 对比后端返回的自定义错误码
                    result.success =
                        if (succeed === String::class.java) body as S else body.parseBody(succeed)
                } else {
                    // 如果不匹配后端自定义错误码则认为请求失败, [result.failure]写入错误异常, 会自动吐司出该信息
                    result.failure =
                        ResponseException(code, jsonObject.getString(message), request) as F
                }
            }
            code in 400..499 -> throw RequestParamsException(code, request) // 请求参数错误
            code >= 500 -> throw ServerResponseException(code, request) // 服务器异常错误
        }
    }

    abstract fun <S> String.parseBody(succeed: Type): S?
}
```

DefaultConvert对于的核心逻辑

1. 判断服务器错误码
1. 判断后端自定义错误码
1. 如果判断错误则创建一个包含错误信息的异常
1. 如果都判断成功则开始解析数据

根据需要你可以在这里加上常见的日志打印, 解密数据, 跳转登录界面等逻辑