### 缓存优势

1. 页面秒开
2. 减少服务器压力
3. 无网络可用

### Net缓存特点

1. 缓存任何请求方式
2. 缓存任何数据, File/图片/JSON/ProtoBuf等
3. 限制最大缓存空间
4. 使用`DiskLruCache`实现, 删除最近最少使用

## 配置缓存

不配置`Cache`是不会启用缓存的
```kotlin
NetConfig.initialize(Api.HOST, this) {
    // Net支持Http缓存协议和强制缓存模式
    // 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小
    cache(Cache(cacheDir, 1024 * 1024 * 128))
}
```

!!! note "判断响应来自缓存"
    如果`Response.cacheResponse`不为null的时, 代表Response来自本地缓存

## Http缓存协议

OkHttp默认的Http缓存协议控制, 要求以下条件

- Get请求方式
- 缓存使用Url为key, 因此Url改变会读不到缓存
- 响应头控制缓存: [Cache-Control](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Cache-Control)

<br>
通过`setCacheControl`可以控制Http缓存协议(原理是添加请求头)

```kotlin
scopeNetLife {
    Post<String>("api") {
        setCacheControl(CacheControl.FORCE_CACHE) // 强制使用缓存
        // setCacheControl(CacheControl.FORCE_NETWORK) // 强制使用网络
        // setCacheControl(CacheControl.Builder().noStore().noCache().build()) // 完全禁止读取/写入缓存
    }.await()
}
```

如果无法实现Http标准缓存协议, 或要缓存Get以外的请求方法, 可以使用`强制缓存模式`来由客户端控制缓存

## 强制缓存模式

无论请求是否存在Http标准缓存协议, 当你设置强制缓存模式时其会无视Http标准缓存协议

```kotlin
scopeNetLife {
    binding.tvFragment.text =
        Post<String>("api") {
            setCacheMode(CacheMode.REQUEST_THEN_READ) // 请求网络失败会读取缓存, 请断网测试
        }.await()
}
```

| 强制缓存模式 | 描述 |
|-|-|
| READ | 只读取缓存, 读不到`NoCacheException` |
| WRITE | 只请求网络, 强制写入缓存 |
| READ_THEN_REQUEST | 先从缓存读取，如果失败再从网络读取, 强制写入缓存 |
| REQUEST_THEN_READ | 先从网络读取，如果失败再从缓存读取, 强制写入缓存 |

## 自定缓存Key

仅`强制缓存模式`有效, 缓存Key默认是`请求方式+URL`后产生的`sha1值`

如果要缓存区别请求参数, 请自定义缓存key

```kotlin
scopeNetLife {
    binding.tvFragment.text =
        Post<String>("api") {
            setCacheMode(CacheMode.REQUEST_THEN_READ) // 请求网络失败会读取缓存, 请断网测试
            setCacheKey("请求热门信息" + params) // 具体值都行
        }.await()
}
```

## 缓存有效期

1. 仅`强制缓存模式`有效, 标准Http缓存协议遵守协议本身的有效期
1. 缓存有效期过期只是让缓存无效, 不会立即删除  <br>根据(最近最少使用)原则在缓存空间达到配置值时删除(即使缓存有效期未到)

```kotlin
scopeNetLife {
    binding.tvFragment.text =
        Post<String>("api") {
            setCacheMode(CacheMode.REQUEST_THEN_READ) // 请求网络失败会读取缓存, 请断网测试
            setCacheValidTime(1, TimeUnit.DAYS) // 缓存仅一天内有效
        }.await()
}
```

## 预览(缓存+网络)

预览又可以理解为回退请求, 一般用于秒开首页或者回退加载数据

```kotlin
scopeNetLife {
    // 然后执行这里(网络请求)
    binding.tvFragment.text = Get<String>("api") {
        setCacheMode(CacheMode.WRITE)
    }.await()
    Log.d("日志", "网络请求")
}.preview(true) {
    // 先执行这里(仅读缓存), 任何异常都视为读取缓存失败
    binding.tvFragment.text = Get<String>("api") {
        setCacheMode(CacheMode.READ)
    }.await()
    Log.d("日志", "读取缓存")
}
```

!!! question "这和加载两次有什么区别?"
    区别是`preview`可以控制以下行为

    1. `breakError` 读取缓存成功后不再处理错误信息, 默认false
    2. `breakLoading` 读取缓存成功后结束加载动画, 默认true
