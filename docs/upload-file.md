上传文件和普通接口请求区别不大

```kotlin
scopeNetLife {
    Post<String>(Api.UPLOAD) {
        param("fileName", assetsFile())
    }.await()
}
```

使用`addUploadListener`添加上传进度监听器, 监听上传进度具体介绍在[进度监听](progress.md)中

## 指定类型

默认会根据文件的后缀名产生MediaType. 但是如果你想自定义MediaType可以直接创建RequestBody参数

```kotlin
scopeNetLife {
    Post<String>(Api.UPLOAD) {
        param("file", assetsFile().toRequestBody("image/webp".toMediaType()))
    }.await()
}
```

## 上传类型

自定义RequestBody可以实现任何数据类型的上传, 但是Net提供常用函数简化`Uri/File`上传

```kotlin
scopeNetLife {
    Post<String>(Api.UPLOAD) {
        // MultiPart 上传
        param("file", Uri)
        param("file", File)

        // 自定义请求体, 会覆盖以上所有请求内容
        body = CustomizerRequestBody()
    }.await()
}
```

直接上传`InputStream`输入流属于不安全行为, 建议你保存到文件后上传,
详细请阅读: [使用文件流上传文件](https://github.com/liangjingkanji/Net/discussions/190)


