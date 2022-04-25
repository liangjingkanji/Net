前面介绍过如何使用AndroidStudio来抓取网络日志. 但是我们可能需要在App使用过程或者让测试人员查看日志

这里介绍一个第三方日志拦截器[chucker](https://github.com/ChuckerTeam/chucker), 他会在Net发起请求后自动在设备通知栏显示网络请求记录, 点击通知可以跳转详情

<img src="https://github.com/ChuckerTeam/chucker/raw/develop/assets/chucker-http.gif" width="250"/>

添加依赖

```groovy
implementation "com.github.chuckerteam.chucker:library:3.5.2"
```

添加拦截器

```kotlin
class App : Application() {

override fun onCreate() {
    super.onCreate()

    NetConfig.initialize("https://github.com/liangjingkanji/", this) {
        // ...
        if (BuildConfig.DEBUG) {
            addInterceptor(
                ChuckerInterceptor.Builder(this@App)
                    .collector(ChuckerCollector(this@App))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
        }
    }
}
```

更多自定义功能请查看[chucker](https://github.com/ChuckerTeam/chucker)主页
