请求下载文件和普通接口请求就有所区别

```kotlin
scopeNetLife {
    Download("download", requireContext().filesDir.path).await()
}
```


这里演示一个包含下载进度监听的
```kotlin
scopeNetLife {
    val filePath = Download("download", requireContext().filesDir.path) {
        // 下载进度回调
        onProgress { progress, byteCount, speed ->
            // 进度条
            seek.progress = progress

            // 格式化显示单位
            val downloadSize = Formatter.formatFileSize(requireContext(), byteCount)
            val downloadSpeed = Formatter.formatFileSize(requireContext(), speed)

            // 显示下载信息
            tv_progress.text = "下载进度: $progress% 已下载: $downloadSize 下载速度: $downloadSpeed"
        }
    }.await()
}
```

Download函数一调用就会开始执行下载文件请求, 然后`await`则会等待下载文件完成然后返回一个文件下载完成后的地址, 示例中即`filePath`.


> 不使用`await`函数则下载报错也不会被Net捕捉到, 将会被忽略, 使用await则会触发Net的错误处理, 终止当前作用域(scope)内其他网络请求, 被Net全局错误处理捕获




