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

!!! success "监听任何进度"
    不仅是泛型为File才有效, 任何请求/响应都可以监听进度


## 监听器

实现`ProgressListener`监听进度信息. 进度信息为`Progress`

### 进度间隔时间

ProgressListener的构造参数`interval`控制触发进度监听器的间隔时间

### 进度信息

| 函数 | 描述 |
|-|-|
| currentBytes | 已完成字节数 |
| totalBytes | 全部大小字节数 |
| intervalBytes | 进度间隔时间内完成的字节数 |
| intervalTime | 距离上次进度变化间隔时间 |
| startElapsedRealtime | 开始下载时间(该时间值开机到现在的毫秒数) |
| speedBytes | 每秒下载字节数 |
| progress | 进度, 1-100 |
| finish | 此次下载/上传是否完成 |
| useTimeSeconds | 已经使用时间, 单位秒 |
| remainTimeSeconds | 估算的剩余时间, 单位秒 |

### 格式化字符串

| 函数 | 描述 |
|-|-|
| currentSize | 已完成大小, 例如: `120kB` 或者 `1.5MB` |
| totalSize | 全部大小 |
| remainSize | 剩余大小 |
| speedSize | 每秒下载大小 |
| useTime | 已使用时间, 格式为: `01:23:04` |
| remainTime | 估算的剩余时间, 格式为: `01:23:04`  |
