轮循器属于项目中常见需求, 网络请求也存在定时网络请求

=== "指定轮循次数/间隔"
    ```kotlin
    interval = Interval(100, 1, TimeUnit.SECONDS).life(this) // 自定义计数器个数的轮循器
    ```
=== "仅轮循间隔"
    ```kotlin
     interval = Interval(1, TimeUnit.SECONDS) // 每秒回调一次, 不会自动结束
    ```
=== "倒计时"
    ```kotlin
    interval = Interval(1, 1, TimeUnit.SECONDS, 5).life(this) // 倒计时轮循器, 当[start]]比[end]值大, 且end不等于-1时, 即为倒计时
    ```

监听轮循器
```kotlin
interval.subscribe {
    tv_fragment.text = it.toString()
}.finish {
    tv_fragment.text = "计时完成" // 最后一位数时同时回调 subscribe/finish
}.start()
```

操作轮循器
```kotlin
interval.start() // 开始轮循器
interval.pause() // 暂停轮循器
interval.resume() // 继续轮循器
interval.reset() // 重置轮循器
interval.switch() // 切换开关
interval.stop() // 停止
```

[完整源码](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/ui/fragment/SuperIntervalFragment.kt)