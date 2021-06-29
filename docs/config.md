全局配置建议在Application中设置

=== "普通初始化"

    ```kotlin
    class App : Application() {
        override fun onCreate() {
            super.onCreate()

            // http://google.com/  这是接口全局域名, 可以使用NetConfig.host进行单独的修改

            NetConfig.init("http://github.com/") {
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

> 配置都是可选项, 不是不初始化就不能使用

在initNet函数作用域中的this属于`OkHttpClient.Builder()`, 可以配置任何OkHttp参数选项

| 函数 | 描述 |
|-|-|
| setLog | 输出网络异常日志 |
| setHost | 全局域名, 和NetConfig.init("Host")函数中的第一个参数等效 |
| setConverter | [转换器](converter.md), 将网络返回的数据转换成你想要的数据结构 |
| setRequestInterceptor | [请求拦截器](interceptor.md), 用于添加全局请求头/参数 |
| setErrorHandler | [全局错误处理](error-handle.md) |
| setDialogFactory | [全局对话框](auto-dialog.md) |

## 动态配置

单例[NetConfig](api/-net/com.drake.net/-net-config/index.html)存储了初始化时的配置, 可以后期动态读写.

例如Retrofit的动态`BaseURL`功能就可以直接修改`NetConfig.host`

<img src="https://i.imgur.com/gOhMDUZ.png" width="480"/>

