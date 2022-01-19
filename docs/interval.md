轮询器(计时器)属于项目中常见需求, 而本框架拥有非常强大的轮询器工具. 支持以下特性

- 正计时
- 倒计时
- 指定次数
- 支持开始/停止/暂停/继续/重置/切换开关
- 仅页面显示时计时
- 页面销毁自动取消

<br>
=== "指定轮循次数/间隔"
    ```kotlin
    interval = Interval(100, 1, TimeUnit.SECONDS).life(this) // 自定义计数器个数的轮询器
    ```
=== "仅轮循间隔"
    ```kotlin
     interval = Interval(1, TimeUnit.SECONDS) // 每秒回调一次, 不会自动结束
    ```
=== "倒计时"
    ```kotlin
    // 倒计时轮询器, 当[start]]比[end]值大, 且end不等于-1时, 即为倒计时
    interval = Interval(1, 1, TimeUnit.SECONDS, 5).life(this)
    ```

监听轮询器
```kotlin
interval.subscribe {
    tvFragment.text = it.toString()
}.finish {
    tvFragment.text = "计时完成" // 最后一位数时同时回调 subscribe/finish
}.start()
```

| Interval函数 | 描述 |
|-|-|
| start | 开始轮询器 |
| stop | 停止 |
| cancel | 取消, 区别于stop, 此函数执行后并不会回调finish |
| pause | 暂停 |
| resume | 继续 |
| reset | 重置 |
| switch | 切换开关 |
| subscribe | 每次计时都会执行该回调 |
| finish | 当计时器完成时执行该回调, 执行stop后也会回调 |
| life | 指定生命周期自动取消轮询器 |
| onlyResumed | 当界面不可见时暂停, 当界面可见时继续 |

[完整源码](https://github.com/liangjingkanji/Net/blob/master/sample/src/main/java/com/drake/net/sample/ui/fragment/SuperIntervalFragment.kt)