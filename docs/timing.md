以下为提供实现定时/限时请求思路, 并不限定只有以下方式


## 限时请求

即超过指定时间后立即取消请求

```kotlin
scopeDialog {
    // 当接口请求在100毫秒内没有完成会抛出异常TimeoutCancellationException
    withTimeout(100) {
        Get<String>(Api.PATH).await()
    }
}.catch {
    Log.e("日志", "请求错误", it) // catch无法接收到CancellationException异常
}.finally {
    Log.e("日志", "请求完成", it) // TimeoutCancellationException属于CancellationException子类故只会被finally接收到
    if (it is TimeoutCancellationException) {
        toast("由于未在指定时间完成请求则取消请求")
    }
}
```

## 定时请求

指定请求10次

```kotlin
scopeNetLife {
    // 每秒请求一次, 总共执行10次
    repeat(20) {
        delay(1000)
        val data = Get<String>(Api.PATH).await()
        if(it = 10) {
            return@repeat
        }
    }
}
```

每秒无限循环请求, 根据某个条件`break`退出

```kotlin
scopeNetLife {
    while (true) {
        delay(1.toDuration(DurationUnit.SECONDS))
        val data = Get<Config>(Api.PATH).await()
        if(data.type = 3) {
            break
        }
    }
}
```