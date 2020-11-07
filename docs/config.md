全局配置推荐在Application中设置
```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // http://182.92.97.186/  这是接口全局域名, 可以使用NetConfig.host读写
        initNet("http://182.92.97.186/") {

            // 大括号内部都是可选项配置

            converter(MoshiConvert()) // 转换器
            cacheEnabled() // 开启缓存
            setLogRecord(BuildConfig.DEBUG) // 日志记录器
            logEnabled = BuildConfig.DEBUG // LogCat异常日志
            addHeader("键" ,"值") // 全局请求头
            setHeader("覆盖键" ,"值") // 全局请求头, 键相同会覆盖
            addParam("键" ,"值") // 全局参数
        }
    }
}
```

> 假设全局参数属于动态获取的, 应当使用拦截器, 在拦截器里面添加

initNet 作用域内可选函数

<img src="https://i.imgur.com/G8W4oDX.png" width="300"/>

## 动态配置

单例对象[NetConfig](api/net/com.drake.net/-net-config/index.md)存储了初始化时的配置, 可以后期动态读写.

例如Retrofit的`baseUrl`功能就可以直接修改`NetConfig.host`

