Net_v2基于[Kalle](https://github.com/yanzhenjie/Kalle)开发, 支持Kalle的9种缓存模式

缓存模式要求在初始化的时候开启

```kotlin
NetConfig.init("http://github.com/") {
    cacheEnabled() // 开启缓存
}
```

=== "请求缓存或网络"
    ```kotlin
    scopeNetLife {
        // 先读取缓存, 如果缓存不存在再请求网络
        tvFragment.text = Get<String>("api", cache = CacheMode.READ_CACHE_NO_THEN_NETWORK).await()
        Log.d("日志", "读取缓存")
    }
    ```
=== "读取缓存然后请求网络"

    ```kotlin
    scopeNetLife {
        // 然后执行这里(网络请求)
        tvFragment.text = Post<String>("api", cache = CacheMode.NETWORK_YES_THEN_WRITE_CACHE).await()
        Log.d("日志", "网络请求")
    }.preview {
        // 先执行这里(仅读缓存), 任何异常都视为读取缓存失败
        tvFragment.text = Get<String>("api", cache = CacheMode.READ_CACHE).await()
        Log.d("日志", "读取缓存")
    }
    ```

预读模式本质上就是创建一个`preview`附加作用域, 里面的所有异常崩溃都会被静默捕捉(算作缓存失败), 会优先于`scope*`执行, 然后再执行scope本身,
而且一旦缓存读取成功(`preview`内部无异常)即使网络请求失败也可以不提醒用户任何错误信息(可配置)

<br>

> `preview`并没有说只能用在网络缓存上, 也可以用于其他的处理场景

<br>

## 缓存模式

缓存模式属于`CacheMod`枚举, 建议开发者浏览缓存模式的源码和注释，有助于理解和更好的使用缓存模式。

| 常量                           | 描述                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| `HTTP`                         | 发起请求前如果本地已经有缓存，如果缓存未过期则返回缓存数据，<br>如果过期则带上缓存头去服务器做校验。如果服务器响应304则返回缓存数据，否则读取服务器数据，<br>根据服务器响应头来决定是否写入缓存数据 |
| `HTTP_YES_THEN_WRITE_CACHE`    | 发起请求前如果本地已经有缓存则带缓存头，服务器响应304才返回缓存数据，否则读取服务器数据，<br>并写入缓存数据 |
| `NETWORK`                      | 发起请求前不管本地是否有缓存，都不会带上缓存头，<br>不论服务器响应头如何，绝不写入缓存数据 |
| `NETWORK_YES_THEN_HTTP`        | 发起请求前不管本地是否有缓存，都不会带上缓存头，<br>根据服务器响应头来决定是否写入缓存数据 |
| `NETWORK_YES_THEN_WRITE_CACHE` | 发起请求前不管本地是否有缓存，都不会带上缓存头，<br>请求成功后写入缓存数据 |
| `NETWORK_NO_THEN_READ_CACHE`   | 发起请求前不管本地是否有缓存，都不会带上缓存头，请求失败后尝试读取缓存, <br>根据服务器响应头来决定是否写入缓存数据 |
| `READ_CACHE `                  | 仅读取缓存                                                   |
| `READ_CACHE_NO_THEN_NETWORK`   | 读取缓存，如果缓存不存在就请求网络，<br>不写入缓存数据 |
| `READ_CACHE_NO_THEN_HTTP`      | 先读取缓存，如果缓存不存在就请求网络，<br>根据服务器响应头来决定是否写入缓存数据 |
| `READ_CACHE_NO_THEN_NETWORK_THEN_WRITE_CACHE`      | 先本地有缓存则读取缓存，如果没有缓存则读取网络并且写入缓存, <br>该模式请求成功后会永久使用缓存, 但你可以指定动态的cacheKey来让缓存失效 <br>例如一天后失效, 可以做到客户端完全控制缓存 |

<br>