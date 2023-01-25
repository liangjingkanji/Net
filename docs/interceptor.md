总共支持两种拦截器

1. `Interceptor`, 支持市面上的所有OkHttp拦截器组件库, 更方便修改请求/响应信息, 可以转发请求
2. `RequestInterceptor`, 部分场景更简单易用的轻量拦截器, 更方便添加全局请求头/参数, 无法转发请求
<br>

> 实际项目中可能存在需求加密请求/解密响应, 非常不建议封装Post/Get等请求动作(低扩展性/增加学习成本), 任何项目需求都可以通过自定义拦截器和转换器实现 <br>
> [常见拦截器示例](https://github.com/liangjingkanji/Net/tree/master/sample/src/main/java/com/drake/net/sample/interceptor)


## 拦截器

添加拦截器

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        NetConfig.initialize("https://github.com/liangjingkanji/Net/", this) {
            addInterceptor(RefreshTokenInterceptor())
        }
    }
}
```

以下为简单演示客户端自动刷新token拦截器

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

RequestInterceptor属于轻量级的请求拦截器, 在每次请求的时候该拦截器都会被触发(无法修改响应信息), 方便添加全局请求头/参数

示例

```kotlin
NetConfig.initialize("https://github.com/liangjingkanji/Net/", this) {
    setRequestInterceptor(object : RequestInterceptor {
        override fun interceptor(request: BaseRequest) {
            request.param("client", "Net")
            request.setHeader("token", "123456")
        }
    })
}
```

可以看到`setRequestInterceptor`是set开头. 仅支持一个请求拦截器, 不像`addInterceptor`支持多个请求拦截器