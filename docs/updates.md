## 3.0
1. 重构代码, 提升稳定性和安全性.
2. 支持OkHttp的所有函数/组件, 可独立升级OkHttp版本
3. 更加强大的下载功能
4. 所有IO读写使用OKIO
5. 更加优雅的函数设计

## 2.3.15
[json]请求体支持Map和Pair参数

## 2.3.14
支持在ViewModel中创建绑定ViewModel生命周期的作用域: scopeLife/scopeNetLife


## 2.3.13
增加函数[json]来便于快速构建Json请求参数

```kotlin
val name = "金城武"
val age = 29
val measurements = listOf(100, 100, 100)

scopeNetLife {

    // 创建JSONObject对象
    tv_fragment.text = Post<String>("api") {
        json(JSONObject().run {
            put("name", name)
            put("age", age)
            put("measurements", JSONArray(measurements))
        })
    }.await()

    // 创建JSON
    tv_fragment.text = Post<String>("api") {
        json("{name:$name, age:$age, measurements:$measurements}")
    }.await()
```

## 2.3.8
日志记录器默认支持输出Json参数

## 2.3.7

新增缓存模式: `READ_CACHE_NO_THEN_NETWORK_THEN_WRITE_CACHE`

先本地有缓存则读取缓存，如果没有缓存则读取网络并且写入缓存, <br>该模式请求成功后会永久使用缓存, 但你可以指定动态的cacheKey来让缓存失效 <br>例如一天后失效, 可以做到客户端完全控制缓存

## 2.3.6

1.  支持无限次数的重定向
2.  默认开启重定向

如果禁止重定向可以创建拦截器取消重定向或者参考`initNet`函数, 但是不添加默认的重定向拦截器