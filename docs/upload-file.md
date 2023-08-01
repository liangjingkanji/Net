```kotlin
scopeNetLife {
    Post<String>(Api.UPLOAD) {
        param("fileName", assetsFile())
    }.await()
}
```

使用`addUploadListener`添加上传进度监听器, 阅读[进度监听](progress.md)章节

## 指定类型

默认根据文件后缀名生成`MediaType`, 如果想自定义MediaType可以直接创建`RequestBody`

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


