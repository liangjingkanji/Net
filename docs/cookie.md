Net使用的是OkHttp的Cookie管理方案(CookieJar), 并且提供持久化存储的Cookie管理实现(PersistentCookieJar)

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NetConfig.initialize("http://github.com/", this) {
            // 添加持久化Cookie
            cookieJar(PersistentCookieJar(this@App))
        }

    }
}
```

PersistentCookieJar可以手动增删Cookie

| 函数 | 描述 |
|-|-|
| addAll | 添加Cookie |
| getAll | 获取某个域名的所有Cookie |
| remove | 删除某个域名下 |
| clear | 删除应用全部Cookie |

PersistentCookieJar使用数据库实现Cookies存储, 你可以指定`dbName`来创建不同的数据库让不同的客户端隔绝Cookie共享
