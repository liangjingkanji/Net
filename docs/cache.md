**网络请求中缓存至关重要, 而Net是最好的**

1. 页面秒开
2. 减少服务器压力
3. 无网络可用

### Net缓存特点

1. 缓存任何请求方式, POST/GET/PUT/HEAD...
2. 缓存任何数据类型, File/图片/JSON/ProtoBuf/...
3. 限制最大缓存体积, 缓存遵守磁盘LRU缓存算法, 当缓存达到限制时, 将会删除最近最少使用的缓存
4. 高性能DiskLruCache来实现统一缓存

## 配置缓存

不配置缓存设置是不会触发缓存的
```kotlin
NetConfig.initialize("https://github.com/liangjingkanji/Net/", this) {
    // ...
    // 本框架支持Http缓存协议和强制缓存模式
    cache(Cache(cacheDir, 1024 * 1024 * 128)) // 缓存设置, 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小
}
```
这也是Net缓存强大之处, 和OkHttp共享缓存但是却可以缓存任何请求方式

## Http缓存协议

这属于OkHttp默认的Http缓存协议控制, 要求满足一定条件

- 请求方式为Get
- URL的MD5值作为Key, 所以一旦URL发生改变即不会算同一缓存
- 存在响应头存在缓存控制: [Cache-Control](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Cache-Control)

<br>
通过指定`CacheControl`也可以控制Http缓存协议(原理是添加请求头)

```kotlin
scopeNetLife {
    Post<String>("api") {
        setCacheControl(CacheControl.FORCE_CACHE) // 强制使用缓存
        // setCacheControl(CacheControl.FORCE_NETWORK) // 强制使用网络
        // setCacheControl(CacheControl.Builder().noStore().noCache().build()) // 完全禁止读取/写入缓存
    }.await()
}
```

还可以指定缓存有效期, 更多使用请查看代码或者搜索[Cache-Control](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Cache-Control)

<br>
如果你后端同事的技术水平无法实现Http标准缓存协议, 或者你需要缓存Get以外的请求方法. 下面我们介绍使用`强制缓存模式`来完全由客户端控制缓存

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

读取缓存失败会引发`NoCacheException`异常(被全局错误处理器接收), 前提是你没有使用读取缓存失败后请求网络模式

| 强制缓存模式 | 描述 |
|-|-|
| READ | 只读取缓存, 本操作并不会请求网络故不存在写入缓存 |
| WRITE | 只请求网络, 强制写入缓存 |
| READ_THEN_REQUEST | 先从缓存读取，如果失败再从网络读取, 强制写入缓存 |
| REQUEST_THEN_READ | 先从网络读取，如果失败再从缓存读取, 强制写入缓存 |

> 如果`response.cacheResponse`不为null的时候即代表response来自于本地缓存, 强制缓存或Http缓存协议都如此

## 自定缓存Key

缓存Key默认是`请求方式+URL`后产生的sha1值(仅强制缓存模式有效), 并不会默认使用请求参数判断

如果你要实现区别请求参数的缓存请自定义缓存key, 如下

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

1. 缓存有效期只针对`强制缓存模式`, 标准Http缓存协议遵守协议本身的有效期
1. 缓存有效期过期只是让缓存无效, 并不会被删除(即无法读取). 缓存删除遵守LRU(最近最少使用)原则在所有缓存体积达到配置的值时自动删除(即使缓存有效期未到)

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

这里可以用到Net的预览模式(preview)来实现, 其实不仅仅是预览缓存也可以用于回退请求

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

> 一般用于秒开首页或者回退加载数据. 我们可以在preview{}只加载缓存. 然后再执行scopeNetLife来请求网络, 做到缓存+网络双重加载的效果

有人可能觉得这和自己加载两次有什么区别, 区别是preview的方法参数可以控制加载

- `breakError` 读取缓存成功后不再处理错误信息, 默认false
- `breakLoading` 读取缓存成功后结束加载动画, 默认true
