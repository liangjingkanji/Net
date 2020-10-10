拦截器一般用于修改请求的参数, Net拦截器和Okhttp使用方式一样
<br>

> 你的业务可能需要请求参数加密或者拼接一串特殊含义的参数, 或者响应信息需要解密. 请不要尝试封装Post或者Get等请求函数(这是蠢材做法), 自定义拦截器和转换器可以应对任何项目需求.


```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initNet("http://182.92.97.186/") {
            addInterceptor { chain -> chain.proceed(chain.request()) }
        }
    }
}
```

添加多个拦截器
```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initNet("http://182.92.97.186/") {
            addInterceptors(RedirectInterceptor(), LogInterceptor())
        }
    }
}
```