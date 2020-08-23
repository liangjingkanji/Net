拦截器一般用于修改请求的参数, Net拦截器和Okhttp使用方式一样

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