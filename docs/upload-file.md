上传文件和普通接口请求区别不大

```kotlin
scopeNetLife {
    Post<String>("upload", requireContext().cacheDir.path) {
        file("fileName", assetsFile())
    }.await()
}
```

使用`addUploadListener`添加上传进度监听器, 监听上传进度具体介绍在[进度监听](progress.md)中
