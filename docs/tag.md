
Q: 什么是标签?

A: 标签就是一个存储在Request对象中的Map集合, 便于Request请求对象携带指定的数据. 该数据可以通过Request在拦截器/转换器/响应体中被获取到, 用于构建区分请求的业务逻辑

<br>
Net中的标签同时支持使用字符串或者Class字节码作为标签的键名. 根据传入类型决定

## 标签使用

### 1) 设置标签

```kotlin hl_lines="2"
scopeNetLife {
    tvFragment.text = Get<String>("api", "标签A"){ // 使用Any::class.java作为键名
        setTag("tagName", "标签B") // 使用字符串作为键名
    }.await()
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
        request.tag("tagName")?.let {
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