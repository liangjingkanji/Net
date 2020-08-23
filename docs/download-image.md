下载图片需要依赖[Glide](https://github.com/bumptech/glide)

这个下载图片函数`DownloadImage`和下载文件函数`Download`不同在于他可以裁剪图片

```kotlin
scopeDialog {
    val file = DownloadImage(NetConfig.host + "download/img", 100, 100).await()
    val uri = Uri.fromFile(file)
    iv_img.setImageURI(uri)
}
```
<br>

DownloadImage的第一个参数Url不会包含`baseUrl`, 所以你可以看到我手动`NetConfig.host + "download/img"`.

!!! note
    这是因为一般项目设计中图片都是单独与服务器之外的地址, 例如CDN图床, 所以默认不自动拼接baseUrl