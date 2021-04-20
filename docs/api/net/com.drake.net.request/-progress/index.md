[net](../../index.md) / [com.drake.net.request](../index.md) / [Progress](./index.md)

# Progress

`data class Progress`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Progress(currentBytes: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, totalBytes: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, intervalBytes: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, intervalTime: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, startElapsedRealtime: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = SystemClock.elapsedRealtime())` |

### Properties

| Name | Summary |
|---|---|
| [currentBytes](current-bytes.md) | 当前已经完成的字节数`var currentBytes: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [intervalBytes](interval-bytes.md) | 进度间隔时间内完成的字节数`var intervalBytes: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [intervalTime](interval-time.md) | 距离上次进度变化间隔时间`var intervalTime: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [speedBytes](speed-bytes.md) | 每秒下载速度, 字节单位`var speedBytes: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [startElapsedRealtime](start-elapsed-realtime.md) | 开始下载的时间`val startElapsedRealtime: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [totalBytes](total-bytes.md) | 当前已经完成的字节数`var totalBytes: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

### Functions

| Name | Summary |
|---|---|
| [currentSize](current-size.md) | 已完成文件大小 根据字节数自动显示内存单位, 例如 19MB 或者 27KB`fun currentSize(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [finish](finish.md) | 是否完成`fun finish(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [progress](progress.md) | 请求或者响应的进度, 值范围在0-100`fun progress(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [remainSize](remain-size.md) | 剩余大小 根据字节数自动显示内存单位, 例如 19MB 或者 27KB`fun remainSize(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [remainTime](remain-time.md) | 剩余时间`fun remainTime(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [remainTimeSeconds](remain-time-seconds.md) | 剩余时间`fun remainTimeSeconds(): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [speedSize](speed-size.md) | 每秒下载速度 根据字节数自动显示内存单位, 例如 19MB 或者 27KB`fun speedSize(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [totalSize](total-size.md) | 文件全部大小 根据字节数自动显示内存单位, 例如 19MB 或者 27KB`fun totalSize(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [useTime](use-time.md) | 已使用时间`fun useTime(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [useTimeSeconds](use-time-seconds.md) | 已使用时间`fun useTimeSeconds(): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
