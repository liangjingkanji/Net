Net支持的`标签`功能, 其标签能够贯穿网络请求的生命周期
<br>
在拦截器(Interceptor)或者转换器(Convert)中都可以通过`request.tag()`获取到标签


## 标签使用

### 1) 设置标签
```kotlin hl_lines="2"
scopeNetLife {
    tv_fragment.text = Get<String>("api", tag = RESPONSE).await()
}
```

### 2) 拦截器中获取标签
```kotlin hl_lines="4"
class NetInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {

        val request = chain.request()

        if (request.tag() == RESPONSE) {
            // 可以打印响应体或者其他逻辑
        }
        return chain.proceed(request)
    }
}
```

### 3) 转换器中获取标签

```kotlin hl_lines="10"
class JsonConvert : DefaultConvert(code = "code", message = "msg", success = "200") {

    override fun <S, F> convert(succeed: Type,
                                failed: Type,
                                request: Request,
                                response: Response,
                                result: Result<S, F>) {
        super.convert(succeed, failed, request, response, result)

        if (request.tag() == RESPONSE) { // 判断标签
            // 执行你的逻辑
        }
    }

    override fun <S> String.parseBody(succeed: Type): S? {
        return Moshi.Builder().build().adapter<S>(succeed).fromJson(this)
    }
}
```



## 多标签
创建一个类继承TAG, 即可通过加减符号来添加多标签

上面例子使用的`RESPONSE`就是一个示例
```kotlin
/**
 * 响应体打印标签
 */
object RESPONSE : TAG()

/**
 * 请求参数打印标签
 */
object REQUEST : TAG()
```

1) 设置标签

```kotlin
scopeNetLife {
    tv_fragment.text = Get<String>("api", tag = RESPONSE + REQUEST).await()
}
```

2) 判断包含标签

```kotlin hl_lines="7 10"
class NetInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()

        val tag = request.tag() as TAG

        if (tag.contains(REQUEST)) {
            // 可以打印响应体或者其他逻辑
        }
        if (tag.contains(RESPONSE)) {
            // 可以打印请求体或者其他逻辑
        }
        return chain.proceed(request)
    }
}
```
<br>

> RESPONSE 和 REQUEST 已经在Net框架中存在, 可以直接使用