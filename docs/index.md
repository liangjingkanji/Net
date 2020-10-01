Net使用协程发起网络, 但是即使不会协程也可以使用该框架

<br>
这里演示发起网络`请求百度网站`内容的三个步骤

1. 创建作用域
1. 发起请求
1. 接收数据

=== "单个请求"
    ```kotlin
    scopeNetLife { // 创建作用域
        // 这个大括号内就属于作用域内部
        val data = Get<String>("http://www.baidu.com/").await() // 发起GET请求并返回`String`类型数据
    }
    ```
=== "串行请求"
    ```kotlin
    scopeNetLife {
        val data = Get<String>("http://0000www.baidu.com/").await() // 请求A 发起GET请求并返回数据
        val data = Get<String>("http://www.baidu.com/").await() // 请求B 将等待A请求完毕后发起GET请求并返回数据
    }
    ```
=== "并发请求"
    ```kotlin
    scopeNetLife {
        // 以下两个网络请求属于同时进行中
        val aDeferred = Get<String>("http://www.baidu.com/") // 发起GET请求并返回一个对象(Deferred)表示"任务A"
        val bDeferred = Get<String>("http://www.baidu.com/") // 发起请求并返回"任务B"

        // 随任务同时进行, 但是数据依然可以按序返回
        val aData = aDeferred.await() // 等待任务A返回数据
        val bData = bDeferred.await() // 等待任务B返回数据
    }
    ```

多个网络请求放在同一个作用域内就可以统一控制, 如果你的多个网络请求毫无关联, 你可以创建多个作用域.

<br>

!!! note
    当`Get`或`Post`等函数调用就会开始发起网络请求, `await`只是等待其请求成功返回结果, 所以如果你在`await`后执行的网络请求,这不属于并发(属于串行)

并发的错误示例
```kotlin hl_lines="3"
scopeNetLife {
    // 请求A
    val aDeferred = Get<String>("http://www.baidu.com/").await()
    // 请求B, 由于上面使用`await()`函数, 所以必须等待A请求返回结果后才会执行B
    val bDeferred = Get<String>("http://www.baidu.com/")

    val bData = bDeferred.await() // 等待任务B返回数据
}
```


<br>
关于JSON解析以及全局URL等初始化配置后面讲解

## 请求参数
关于请求参数参数
```kotlin
scopeNetLife { // 创建作用域
    // 这个大括号内就属于作用域内部
    val data = Get<String>("http://www.baidu.com/"){
        param("u_name", "drake")
        param("pwd", "123456")
    }.await() // 发起GET请求并返回`String`类型数据
}
```

|请求函数|描述|
|-|-|
|`param`|请求体参数|
|`path`|路径|
|`urlParam`|Url参数|
|`file`|上传文件|
|`binary`|二进制|
|`binaries`|多个二进制|
|`body`|自定义请求体|


## RESTFUL
Net支持RestFul设计风格

```kotlin
private fun GET() {
    scopeNetLife {
        tv_fragment.text = Get<String>("http://www.baidu.com/").await()
    }
}

private fun POST() {
    scopeNetLife {
        tv_fragment.text = Post<String>("http://www.baidu.com/").await()
    }
}

private fun HEAD() {
    scopeNetLife {
        tv_fragment.text = Head<String>("http://www.baidu.com/").await()
    }
}

private fun PUT() {
    scopeNetLife {
        tv_fragment.text = Put<String>("http://www.baidu.com/").await()
    }
}

private fun PATCH() {
    scopeNetLife {
        tv_fragment.text = Patch<String>("http://www.baidu.com/").await()
    }
}

private fun DELETE() {
    scopeNetLife {
        tv_fragment.text = Delete<String>("http://www.baidu.com/").await()
    }
}

private fun TRACE() {
    scopeNetLife {
        tv_fragment.text = Trace<String>("http://www.baidu.com/").await()
    }
}

private fun OPTIONS() {
    scopeNetLife {
        tv_fragment.text = Options<String>("http://www.baidu.com/").await()
    }
}
```

## 函数

### 异步请求
即会在IO线程执行网络请求, 要求在作用域内执行

|异步请求函数|描述|
|-|-|
| [Get](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-get.md)|标准REST FUL|
| [Post](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-post.md)|标准REST FUL|
| [Head](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-head.md)|标准REST FUL|
| [Options](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-options.md)|标准REST FUL|
| [Trace](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-trace.md)|标准REST FUL|
| [Delete](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-delete.md)|标准REST FUL|
| [Put](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-put.md)|标准REST FUL|
| [Patch](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-patch.md)|标准REST FUL|
| [Download](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-download.md)|下载文件(默认Get)|
| [DownloadBody](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-download-body.md)|下载文件(默认Post, 附带请求体)|
| [DownloadImage](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-download-image.md)|下载图片(可指定图片大小), 如果使用要求依赖Glide|

### 同步请求

即在当前线程执行, 会阻塞当前线程

|同步请求函数|描述|
|-|-|
|syncGet|标准REST FUL|
|syncPost|标准REST FUL|
|syncHead|标准REST FUL|
|syncOptions|标准REST FUL|
|syncTrace|标准REST FUL|
|syncDelete|标准REST FUL|
|syncPut|标准REST FUL|
|syncPatch|标准REST FUL|
|syncDownload|下载文件(默认Get)|
|syncDownloadBody|下载文件(默认Post, 附带请求体)|
|syncDownloadImage|下载图片(可指定图片大小), 如果使用要求依赖Glide|