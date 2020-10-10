上传文件和普通接口请求区别不大

```kotlin
scopeNetLife {
    Post<String>("upload", requireContext().cacheDir.path) {
        file("fileName", getFile())
    }.await()
}
```

这里演示上传一个包含上传进度监听的文件

```kotlin
scopeNetLife {
    Post<String>("upload") {

        val saveFile = getFile()
        val form = FormBody.newBuilder().file("file", saveFile).build()

        form.onProgress { origin, progress ->

            seek.progress = progress // 进度条

            // 格式化显示单位
            val downloadSize =
                android.text.format.Formatter.formatFileSize(requireContext(), 23)
            val downloadSpeed =
                android.text.format.Formatter.formatFileSize(requireContext(), 23)

            // 显示下载信息
            tv_progress.text = "上传进度: $progress% 已下载: $downloadSize 下载速度: $downloadSpeed"
        }

        body(form)

    }.await()
}
```

<br>

> 普通字段接口请求也可以通过这种方式监听上传参数进度