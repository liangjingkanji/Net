Net总共支持两种拦截器

1. Interceptor, 支持市面上的所有OkHttp拦截器组件库, 可以修改任何请求响应信息, 可以转发请求
2. RequestInterceptor, 更简单易用的轻量拦截器, 一般用于添加全局请求头/参数, 无法转发请求
<br>

> 你的业务可能需要加密解密. 但请不要封装Post/Get等请求动作(这是不明智的做法) <br>
  建议自定义拦截器和转换器可以应对任何项目需求, 同时更符合架构设计

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


在拦截器中可以使用以下函数复制请求/响应体

| 函数 | 描述 |
|-|-|
| peekString | 可以复制截取RequestBody/ResponseBody, 且返回String |
| logString | 等效于上面函数, 但是Response仅支持文本/JSON, Request仅支持FormBody |

## 请求拦截器

RequestInterceptor属于轻量级的请求拦截器, 在每次请求的时候该拦截器都会被触发, 但是无法修改响应信息 <br>

一般用于添加全局的参数和请求头

初始化时添加请求拦截器的示例代码

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