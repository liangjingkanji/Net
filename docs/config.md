全局配置推荐在Application中设置
```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // http://182.92.97.186/  这是接口全局域名, 可以使用NetConfig.host读写
        initNet("http://182.92.97.186/") {

            // 大括号内部都是可选项配置

            converter(JsonConvert()) // 转换器
            cacheEnabled() // 开启缓存
            logEnabled = false // 关闭异常信息打印
        }
    }
}
```

initNet 作用域内可选函数

<img src="https://i.imgur.com/G8W4oDX.png" width="60%"/>

## 动态配置

单例对象[NetConfig](api/net/com.drake.net/-net-config/index.md)存储了初始化时的配置, 可以后期动态读写.

例如Retrofit的`baseUrl`功能就可以直接修改`NetConfig.host`

