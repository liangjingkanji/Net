
不同于一些网络框架使用数据库甚至序列化json等来缓存, 他们无法限制缓存体积/性能也差, 甚至只能缓存json或者字符串

而NET使用高性能DiskLruCache来实现统一缓存. 所有缓存遵守磁盘LRU缓存算法, 当缓存达到限制时, 将会删除最近最少使用的缓存

可以缓存任何数据类型, 包括文件/json/protobuf/...


## 配置缓存

不配置缓存设置是不会触发缓存的
```kotlin
NetConfig.initialize("https://github.com/liangjingkanji", this) {
    // ...
    // 本框架支持Http缓存协议和强制缓存模式
    cache(Cache(cacheDir, 1024 * 1024 * 128)) // 缓存设置, 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小
}
```
这也是Net缓存强大之处, 和OkHttp共享缓存但是却可以缓存任何请求方式

## Http缓存协议

这属于OkHttp默认的Http缓存协议控制, 要求满足一定条件

- 请求方式为Get
- 存在响应头存在缓存控制: [Cache-Control](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Cache-Control)
- URL的MD5值作为Key

考虑到你的后端同事的技术水平可能无法实现标准缓存协议, 下面我们可以使用强制缓存模式来完全由客户端控制缓存

## 强制缓存模式

```kotlin
scopeNetLife {
    binding.tvFragment.text =
        Post<String>("api") {
            setCacheMode(CacheMode.REQUEST_THEN_READ) // 请求网络失败会读取缓存, 请断网测试
            // setCacheKey("自定义缓存KEY")
            // 通过自定义Key可以设置一个每天会失效的缓存. 但超出缓存限制后还是会遵守最近最少使用删除策略
            // setCacheKey("自定义缓存KEY" + System.currentTimeMillis() / TimeUnit.DAYS.toMillis(1))
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

> 在okHttp中`response.cacheResponse`不为null的时候即代表response来自于本地缓存, 强制缓存或Http缓存协议都如此

## 缓存+网络

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
