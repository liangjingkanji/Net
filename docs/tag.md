Net支持两种方式携带数据, 贯穿整个请求流程(请求/拦截器/转换器)

- tag: `HashMap<Class<*>, Any?>` 标签
- extra: `HashMap<String, Any?>` 额外数据

两者区别为存储时key是`Class`还是`String`, 自由选择

## 标签使用

### 1. 写入标签

```kotlin hl_lines="2"
scopeNetLife {
    tvFragment.text = Get<String>("api", "标签A"){ // 使用Any::class.java作为键名
        // tag("标签A") 等效上一行的参数 "标签A"
        setExtra("tagName", "标签B") // 写入额外数据
    }.await()
}
```

### 2. 拦截器中读取标签
```kotlin hl_lines="4"
class MyInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        request.tag()?.let {
            // 读取标签
        }
        request.extra("tagName")?.let {
           // 读取额外数据
        }
        return chain.proceed(request)
    }
}
```

### 3. 转换器中读取标签

```kotlin hl_lines="4"
class MyConvert : NetConvert {

    override fun <R> onConvert(succeed: Type, response: Response ): R? {
        response.request.tag()?.let{
            // 读取标签
        }
    }
}
```

<br>

通过`Request`可以读写数据

| 方法 | 描述 |
|-|-|
| setExtra | 写入额外数据 |
| extra | 读取额外数据 |
| extras | 全部额外数据 |
| tag | 读取/写入标签 |
| tagOf | 读取/写入标签, 为`tag()`的泛型替代 |
| tags | 全部标签 |

## 多个标签

```kotlin
scopeNetLife {
    Get<String>("api"){
        setExtra("person", Person()) // 使用Request.extra("person")读取
        tag(User()) // 等同于tag(Any::class.java, User()), 使用Request.tag()读取
        tag(User::class.java, User()) // 使用Request.tag(User::class.java)读取
    }.await()
}
```

得到标签

```kotlin hl_lines="4"
class MyInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        request.extra("person")?.let {
            // it既为Person对象
        }
        request.tagOf<User>() // 结果为User()
        request.tag() // 结果为User()
        return chain.proceed(request)
    }
}
```