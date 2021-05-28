上传文件和普通接口请求区别不大

```kotlin
scopeNetLife {
    Post<String>("upload") {
        param("fileName", assetsFile())
    }.await()
}
```

使用`addUploadListener`添加上传进度监听器, 监听上传进度具体介绍在[进度监听](progress.md)中

## 指定类型

默认会根据文件的后缀名产生MediaType. 但是如果你想自定义MediaType可以直接创建RequestBody参数

```kotlin
scopeNetLife {
    Post<String>("upload") {
        param("file", assetsFile().toRequestBody("image/webp".toMediaType()))
    }.await()
}
```
