全局配置建议在Application的onCreate函数中配置

## 初始配置

=== "普通初始化"

    ```kotlin
    class App : Application() {
        override fun onCreate() {
            super.onCreate()
    
            // http://google.com/  这是接口全局域名, 可以使用NetConfig.host进行单独的修改
            NetConfig.initialize("https://github.com/liangjingkanji/Net/", this) {
                // 超时配置, 默认是10秒, 设置太长时间会导致用户等待过久
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                setDebug(BuildConfig.DEBUG)
                setConverter(SerializationConverter())
            }
        }
    }
    ```

=== "OkHttpClient.Builder"

    ```kotlin
    class App : Application() {
        override fun onCreate() {
            super.onCreate()
            // http://google.com/  这是接口全局域名, 可以使用NetConfig.host进行单独的修改
            val okHttpClientBuilder = OkHttpClient.Builder()
                .setDebug(BuildConfig.DEBUG)
                .setConverter(SerializationConverter())
                .addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
    
            NetConfig.initialize("https://github.com/liangjingkanji/Net/", okHttpClientBuilder)
        }
    }
    ```

> 配置都是可选项, 不是不初始化就不能使用. 在Xposed项目中可能需要使用 `NetConfig.app = this`

在initNet函数作用域中的this属于`OkHttpClient.Builder()`, 可以配置任何OkHttpClient.Builder的属性以外还支持以下Net独有配置

| 函数 | 描述 |
|-|-|
| setDebug | 是否输出网络日志, 和`LogRecordInterceptor`互不影响  |
| setSSLCertificate | 配置Https证书 |
| trustSSLCertificate | 信任所有Https证书 |
| setConverter | [配置数据转换器](converter.md), 将网络返回的数据转换成你想要的数据结构 |
| setRequestInterceptor | [配置请求拦截器](interceptor.md), 适用于添加全局请求头/参数 |
| setErrorHandler | [配置全局错误处理](error-global.md) |
| setDialogFactory | [配置全局对话框](auto-dialog.md) |

## 重试次数

默认情况下你设置超时时间即可, OkHttp内部也有重试机制.

但是个别开发者需求指定重试次数则可以添加`RetryInterceptor`拦截器即可实现失败以后会重试指定次数

```kotlin
NetConfig.initialize("https://github.com/liangjingkanji/Net/") {
    // ... 其他配置
    addInterceptor(RetryInterceptor(3)) // 如果全部失败会重试三次
}
```


## 修改配置

单例[NetConfig](api/-net/com.drake.net/-net-config/index.html)存储初始化时的配置, 可以随时修改配置

例如Retrofit的动态`baseURL`功能就可以直接修改`NetConfig.host`

```kotlin
NetConfig.host = "https://github.com/liangjingkanji/Net/"
```


## 动态域名

如果请求时传入的是路径(例如`/api/index`)那么会自动和初始化时的Host拼接. 但是如果传入的是以`http/https`开头的全路径那么则不会与Host进行拼接

```kotlin
scopeNetLife {
    val data = Get<String>("https://github.com/liangjingkanji/Net/").await()
}
```


