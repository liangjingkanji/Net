Net支持上传/下载的进度监听, 且具备完善的进度信息


## 上传进度监听

```kotlin
scopeNetLife {
    Post<String>("https://download.sublimetext.com/Sublime%20Text%20Build%203211.dmg") {
        param("file", assetsFile())
        addUploadListener(object : ProgressListener() {
            override fun onProgress(p: Progress) {
                seek.post {
                    seek.progress = p.progress()
                    tvProgress.text =
                        "上传进度: ${p.progress()}% 上传速度: ${p.speedSize()}     " +
                                "\n\n文件大小: ${p.totalSize()}  已上传: ${p.currentSize()}  剩余大小: ${p.remainSize()}" +
                                "\n\n已使用时间: ${p.useTime()}  剩余时间: ${p.remainTime()}"
                }
            }
        })
    }.await()
}
```



## 下载进度监听

```kotlin
scopeNetLife {
    val file =
        Get<File>("https://download.sublimetext.com/Sublime%20Text%20Build%203211.dmg") {
            setDownloadFileName("net.apk")
            setDownloadDir(requireContext().filesDir)

            addDownloadListener(object : ProgressListener() {
                override fun onProgress(p: Progress) {
                    seek?.post {
                        val progress = p.progress()
                        seek.progress = progress
                        tvProgress.text =
                            "下载进度: $progress% 下载速度: ${p.speedSize()}     " +
                                    "\n\n文件大小: ${p.totalSize()}  已下载: ${p.currentSize()}  剩余大小: ${p.remainSize()}" +
                                    "\n\n已使用时间: ${p.useTime()}  剩余时间: ${p.remainTime()}"
                    }
                }
            })
        }.await()
}
```

> 无论是上传还是下载的进度监听不仅仅是泛型为File对象才有效, 任何请求/响应都会被监听到进度


## 监听器

通过继承`ProgressListener`抽象类实现监听进度信息. 进度信息为`Progress`对象

### 进度间隔时间

ProgressListener的构造参数`interval`控制触发进度监听器的间隔时间, 默认是500毫秒. 毕竟进度监听不需要太频繁的调用影响性能.

### 进度信息

| 函数 | 描述 |
|-|-|
| currentBytes | 已完成字节数 |
| totalBytes | 全局字节数 |
| intervalBytes | 监听器间隔时间内的完成字节数 |
| intervalTime | 监听器的间隔时间 |
| startElapsedRealtime | 开始下载时间(该时间值开机到现在的毫秒数) |
| speedBytes | 每秒下载字节数 |
| progress | 进度, 1-100 |
| finish | 此次下载/上传是否完成 |
| useTimeSeconds | 已经使用时间, 单位秒 |
| remainTimeSeconds | 估算的剩余时间, 单位秒 |

同时提供了一系列字符串格式函数, 都是返回已经添加好单位

| 函数 | 描述 |
|-|-|
| currentSize | 已完成大小, 格式为: `120kB` 或者 `1.5MB` |
| totalSize | 全部大小 |
| remainSize | 剩余大小 |
| speedSize | 每秒下载大小 |
| useTime | 已使用时间, 格式为: `01:23:04` |
| remainTime | 估算的剩余时间, 格式为: `01:23:04`  |
