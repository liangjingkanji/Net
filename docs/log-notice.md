使用三方库 [Chucker](https://github.com/ChuckerTeam/chucker) 在通知栏记录网络请求日志

<figure markdown>
  ![](https://github.com/ChuckerTeam/chucker/raw/main/assets/chucker-http.gif){ width="300" }
  <a href="https://github.com/ChuckerTeam/chucker" target="_blank"><figcaption>Chucker</figcaption></a>
</figure>

依赖

```groovy
implementation "com.github.chuckerteam.chucker:library:3.5.2"
```

添加拦截器

```kotlin
NetConfig.initialize(Api.HOST, this) {
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
```

自定义功能浏览 [Chucker](https://github.com/ChuckerTeam/chucker) 官网
