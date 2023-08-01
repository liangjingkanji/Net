泛型改为`File`即可

```kotlin
scopeNetLife {
    val file = Get<File>("download").await()
}
```

## 下载选项

丰富的下载定制方案, 并且在不断更新

```kotlin
scopeNetLife {
    val file =
        Get<File>("https://download.sublimetext.com/Sublime%20Text%20Build%203211.dmg") {
            setDownloadFileName("net.apk")
            setDownloadDir(requireContext().filesDir)
            setDownloadMd5Verify()
        }.await()
}
```

| 配置选项                    | 描述                                                         |
| --------------------------- | ------------------------------------------------------------ |
| addDownloadListener         | [下载进度监听器](progress.md)                                |
| setDownloadFileName         | 下载文件名                                                   |
| setDownloadDir              | 下载目录  |
| setDownloadMd5Verify        | 下载文件MD5校验 |
| setDownloadFileNameConflict | 下载文件同名冲突解决 |
| setDownloadFileNameDecode   | 文件名Url解码中文 |
| setDownloadTempFile         | 临时文件名     |

## 重复下载

防止重复下载有以下方式

| 函数     | 描述                                                      |
| -------- | --------------------------------------------------------- |
| 文件判断 | 判断本地是否存在同名文件                                  |
| 缓存模式 | 开启缓存, 占用设备两份空间(缓存/下载成功文件都占空间)     |
| MD5校验  | 服务器返回`Content-MD5`, 客户端开启`setDownloadMd5Verify` |