每个请求都会存在一个客户端对象, 既OkHttpClient

Net在全局维护了一个OkHttpClient对象, 在NetConfig.okHttpClient的字段

```kotlin
object NetConfig {
    var okHttpClient: OkHttpClient
}
```

> 当然也支持创建一个新的客户端来发起请求(配置区别于全局客户端)

## 全局OkHttpClient

```kotlin
NetConfig.init("http://github.com/") {
    // 此处this即为OkHttpClient.Builder
}
```


## 单例OkHttpClient

每个请求可能存在独立的OkHttpClient配置, 我们可以单例配置客户端选项

1. 在全局的OkHttpClient配置基础下修改

```kotlin
scopeNetLife {
    tv_response.text = Get<String>("https://github.com/") {
        setClient {
            // 此处this即为OkHttpClient.Builder
            trustCertificate()
        }
    }.await()
}
```

2. 完全重新创建一个OkHttpClient, 一般情况不推荐重新创建一个OkHttpClient, 因为一个新的OkHttpClient会重新创建线程池/连接池等造成内存消耗等

```kotlin
scopeNetLife {
    tv_response.text = Get<String>("https://github.com/") {
        okHttpClient = OkHttpClient.Builder().build()
    }.await()
}
```