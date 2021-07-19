## 简单下载

下载文件和普通的接口请求唯一区别就是泛型不同

```kotlin
scopeNetLife {
    val file = Get<File>("download").await()
}
```

Download函数一调用就会开始执行下载文件请求, 然后`await`则会等待下载文件完成然后返回一个File对象

## 下载选项

支持丰富的下载定制方案, 并且会不断地更新完善

=== "下载文件"
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

=== "下载并安装"
    ```kotlin hl_lines="7"
    scopeNetLife {
        Get<File>("https://download.sublimetext.com/Sublime%20Text%20Build%203211.dmg") {
            setDownloadFileName("net.apk")
            setDownloadDir(requireContext().filesDir)
            setDownloadMd5Verify()
        }.await().install()
    }
    ```

配置选项

| 函数 | 描述 |
|-|-|
| setDownloadFileName | 下载的文件名称 |
| setDownloadDir | 下载保存的目录 |
| setDownloadMd5Verify | 下载是否开启MD5校验, 如果服务器返回 "Content-MD5"响应头和制定路径已经存在的文件MD5相同是否直接返回File, 不会重新下载 |
| setDownloadFileNameConflict | 下载文件名如果在指定路径下存在同名会自动重新命名, 例如`file_name(1).apk` |
| setDownloadFileNameDecode | 文件名称是否使用URL解码, 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称 |
| setDownloadTempFile | 下载是否使用临时文件, 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名 |
| addDownloadListener | 下载进度监听器, 具体介绍在[进度监听](progress.md)中 |

> 不使用`await`函数则下载报错也不会被Net捕捉到, 将会被忽略, 使用await则会触发Net的错误处理, 终止当前作用域(scope)内其他网络请求, 被Net全局错误处理捕获




