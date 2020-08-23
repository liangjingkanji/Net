请求下载文件和普通接口请求就有所区别

```kotlin
scopeNetLife {
    Download("download", requireContext().filesDir.path).await()
}
```


这里演示一个包含下载进度监听的
```kotlin
scopeNetLife {
    Download("download", requireContext().filesDir.path) {
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