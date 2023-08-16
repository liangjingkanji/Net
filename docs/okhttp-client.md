Net全局持有一个`OkHttpClient`对象发起请求

```kotlin
object NetConfig {
    var okHttpClient: OkHttpClient
}
```

## 全局

```kotlin
NetConfig.initialize(Api.HOST, this) {
    // 此处this即为OkHttpClient.Builder
}
```


## 单例

单独指定当前请求客户端

=== "修改全局客户端"
    ```kotlin
    scopeNetLife {
        tv_response.text = Get<String>(Api.PATH) {
            setClient {
                // 此处this即为OkHttpClient.Builder
                trustCertificate()
            }
        }.await()
    }
    ```
     在全局`OkHttpClient`基础上修改

=== "创建新客户端"
    ```kotlin
    scopeNetLife {
        tv_response.text = Get<String>(Api.PATH) {
            okHttpClient = OkHttpClient.Builder().build()
        }.await()
    }
    ```
    创建新的OkHttpClient, 一般不使用, 因为新OkHttpClient会重新创建线程池/连接池等造成内存消耗