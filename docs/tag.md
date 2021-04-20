Net的标签完美的扩展的OkHttp的标签功能

<br>
在拦截器(Interceptor)或者转换器(NetConvert)中都可以通过`request.tag(name)`获取到标签对象


## 标签使用

### 1) 设置标签
```kotlin hl_lines="2"
scopeNetLife {
    tv_fragment.text = Get<String>("api", "我是一个标签").await()
}
```

### 2) 拦截器中获取标签
```kotlin hl_lines="4"
class MyInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        request.tag()?.let {
            // 获取标签做任何事
        }
        return chain.proceed(request)
    }
}
```

### 3) 转换器中获取标签

```kotlin hl_lines="4"
class MyConvert : NetConvert {

    override fun <R> onConvert( succeed: Type, response: Response ): R? {
        response.request.tag()?.let{
            // 获取标签做任何事
        }
    }
}
```

<br>

我们通过Request的函数可以设置和读取标签

| 函数 | 描述 |
|-|-|
| setTag | 设置标签 |
| tag | 读取标签 |


## 设置多个标签

```kotlin
scopeNetLife {
    Get<String>("api"){
        setTag("person", Person()) // 使用Request.tag("person")获取
        setTag(User()) // 使用Request.tag()直接获取
    }.await()
}
```

得到标签

```kotlin hl_lines="4"
class MyInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        request.tag("person")?.let {
            // it既为Person对象
        }
        return chain.proceed(request)
    }
}
```