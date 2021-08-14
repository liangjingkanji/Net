全局配置建议在Application的onCreate函数中配置

## 初始配置

=== "普通初始化"

    ```kotlin
    class App : Application() {
        override fun onCreate() {
            super.onCreate()
    
            // http://google.com/  这是接口全局域名, 可以使用NetConfig.host进行单独的修改
    
            NetConfig.init("http://github.com/") {
    
                // 超时设置
                connectTimeout(2, TimeUnit.MINUTES)
                readTimeout(2, TimeUnit.MINUTES)
                writeTimeout(2, TimeUnit.MINUTES
    
                setLog(BuildConfig.DEBUG) // 作用域发生异常是否打印
                setConverter(GsonConvert()) // 转换器
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
                .setLog(BuildConfig.DEBUG)
                .setConverter(GsonConvert())
                .addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
    
            NetConfig.init("http://github.com/", okHttpClientBuilder)
        }
    }
    ```

> 配置都是可选项, 不是不初始化就不能使用. 在Xposed项目中可能需要使用 `NetConfig.app = this`

在initNet函数作用域中的this属于`OkHttpClient.Builder()`, 可以配置任何OkHttp参数选项

| 函数 | 描述 |
|-|-|
| setLog | 输出网络异常日志 |
| setHost | 全局域名, 和NetConfig.init("Host")函数中的第一个参数等效 |
| setConverter | [转换器](converter.md), 将网络返回的数据转换成你想要的数据结构 |
| setRequestInterceptor | [请求拦截器](interceptor.md), 用于添加全局请求头/参数 |
| setErrorHandler | [全局错误处理](error-handle.md) |
| setDialogFactory | [全局对话框](auto-dialog.md) |

## 重试次数

默认情况下你设置超时时间即可, OkHttp内部也有重试机制.

但是个别开发者需求指定重试次数则可以添加`RetryInterceptor`拦截器即可实现失败以后会重试指定次数

```kotlin
NetConfig.init("http://github.com/") {
    // ... 其他配置
    addInterceptor(RetryInterceptor(3)) // 如果全部失败会重试三次
}
```


## 动态配置

单例[NetConfig](api/-net/com.drake.net/-net-config/index.html)存储了初始化时的配置, 可以后期动态读写.

例如Retrofit的动态`baseURL`功能就可以直接修改`NetConfig.host`

```kotlin
NetConfig.host = "https://github.com/"
```

<img src="https://i.loli.net/2021/08/14/jZyaU5IVhPipWEr.png" width="480"/>

