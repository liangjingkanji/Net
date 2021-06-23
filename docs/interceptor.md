拦截器(Interceptor)一般用于修改请求的参数或者进行请求转发/重试. Net就是使用的OkHttp的拦截器. 所以支持市面上的所有OkHttp拦截器组件库
<br>

> 你的业务可能需要请求参数加密或者响应信息需要解密. 请尽可能不要封装Post/Get等请求动作(这是不明智的做法) <br>
  自定义拦截器和转换器可以应对任何项目需求. 同时更符合项目设计


```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        NetConfig.init("http://github.com/") {
            addInterceptor { chain -> chain.proceed(chain.request()) }
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

RequestInterceptor属于轻量级的请求拦截器, 在每次请求的时候该拦截器都会被触发. 一般用于添加全局的参数和请求头

初始化时添加请求拦截器的示例代码

```kotlin
NetConfig.init("http://github.com/") {
    setRequestInterceptor(object : RequestInterceptor {
        override fun interceptor(request: BaseRequest) {
            request.param("client", "Net")
            request.setHeader("token", "123456")
        }
    })
}
```

可以看到`setRequestInterceptor`是set开头. 仅支持一个请求拦截器, 不像`addInterceptor`支持多个请求拦截器