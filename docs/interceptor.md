根据使用场景选择

1. `Interceptor`: 支持三方OkHttp拦截器组件, 允许修改请求/响应信息, 可以转发请求
2. `RequestInterceptor`: Net独有的轻量级拦截器, 允许修改全局请求头/请求参数, 无法转发请求
<br>

!!! Failure "禁止无效封装"
    不应为全局参数/加密等封装请求方法, 应自定义拦截器/转换器来实现, [常见拦截器示例](https://github.com/liangjingkanji/Net/tree/master/sample/src/main/java/com/drake/net/sample/interceptor)



## 拦截器

添加拦截器

```kotlin
NetConfig.initialize(Api.HOST, this) {
    addInterceptor(RefreshTokenInterceptor())
}
```

演示客户端自动刷新token的拦截器

```kotlin
/**
 * 演示如何自动刷新token令牌
 */
class RefreshTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request) // 如果token失效

        return synchronized(RefreshTokenInterceptor::class.java) {
            if (response.code == 401 && UserConfig.isLogin && !request.url.pathSegments.contains("token")) {
                val json = Net.get("token").execute<String>() // 同步刷新token
                UserConfig.token = JSONObject(json).optString("token")
                chain.proceed(request)
            } else {
                response
            }
        }
    }
}
```

## 请求拦截器

轻量级拦截器(`RequestInterceptor`), 其Api更适合添加全局请求头/参数

```kotlin
NetConfig.initialize(Api.HOST, this) {
    setRequestInterceptor(object : RequestInterceptor {
        override fun interceptor(request: BaseRequest) {
            request.param("client", "Net")
            request.setHeader("token", "123456")
        }
    })
}
```

可以看出`setRequestInterceptor`仅支持一个, `addInterceptor`支持多个拦截器