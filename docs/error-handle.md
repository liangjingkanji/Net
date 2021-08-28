Net具备完善的错误处理机制, 默认情况发生错误会执行

1. Logcat 显示异常信息
2. Toast 显示错误信息

要改变以上错误处理流程就需要自定义错误处理器

## 单例处理异常
```kotlin
scope {
    val data = Get<String>("http://www.thisiserror.com/").await()
}.catch {
    // 协程内部发生错误回调, it为异常
    handleError(it) // 该函数会将错误再次传递给全局错误回调处理
}.finally {
    // 协程内全部执行完成回调(包括子协程), it为异常
}
```

以下函数幕后字段`it`为异常对象, 如果正常完成it则为null. 如果属于请求被手动取消则it为`CancellationException`

| 函数 | 描述 |
|-|-|
| catch | 作用域被`catch`则不会被传递到全局异常处理回调中: [全局处理异常](error-handle.md), 除非使用`handleError`再次传递给全局 |
| finally | 同样可以获取到异常对象, 且不影响全局异常回调处理 |

## 全局处理异常

Net默认就会处理异常错误避免崩溃. 但是如果你想要自定义或者监听全局错误, 你可以覆盖默认的错误处理.  在`initNet`或者`NetConfig.errorHandler`函数里面自定义全局错误处理

默认情况下不需要去定义错误处理, 因为`NetErrorHandler`默认实现适用于大部分情况的错误处理.

|场景|处理函数|处理方式|
|-|-|-|
|普通网络请求/自动加载框|`onError`|`toast`提示后端定义的错误消息|
|自动缺省页|`onStateError`|部分错误信息`toast`|

示例

```kotlin
NetConfig.init("http://localhost:80/") {

    setErrorHandler(object : NetErrorHandler() {
        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onStateError(e: Throwable, view: View) {
            super.onStateError(e, view)
        }
    })
}
```

缺省页不需要所有的错误信息都吐司(toast)提示, 因为错误页可能已经展示错误信息, 所以这里两者处理的函数区分.

但是所有的错误信息和错误码都会在LogCat控制台看到, 具体查看[异常追踪](exception-track.md).

> 本章末尾有默认实现的源码, 可供参考或者理解: [默认处理](#_2)


## 异常对象

以下为Net内部常见发生的异常对象. 不代表全部异常, 例如不包含开发者自己定义的异常

> `scope/scopeLife`不会触发任何全局错误NetErrorHandler, 请使用单例错误处理方式`catch`, 因为`scope`用于处理异步任务,不应当用于网络请求


| 异常 | 描述 |
|-|-|
| NetException | 未知的网络异常, 一般情况应当继承他来创建自定义的网络异常  |
| HttpFailureException | Http请求错误, Http请求失败(onFailure) |
| HttpResponseException | Http请求成功后发生的错误, Http请求成功但发生异常(onResponse) |
| URLParseException | 地址错误 |
| NetUnknownHostException | 域名错误 |
| NetSocketTimeoutException | 连接超时 |
| NetConnectException | 连接服务器失败 |
| DownloadFileException | 下载文件异常 |
| ConvertException | 解析错误, `NetConvert`中发生的未捕获异常都算解析错误 |
| RequestParamsException | 请求参数错误 `400 - 499` 范围内错误码 |
| ServerResponseException | 服务器错误 `500 - 599` 范围内错误码 |
| ResponseException | 错误码异常, 一般应用于后端返回的错误码和其定义的成功码不匹配 |
| NullPointerException | 空指针, 一般是在作用域内操作一个空的对象 |

假设你重写`NetConverter`可以改变异常发生的条件, 当然你在转换器或者拦截器中抛出任何异常都会被捕获或者全局处理 <br>
转换器中发生的所有异常除非是NetException的子类否则都将被ConvertException包裹(即捕获的是ConvertException).


## 覆盖默认错误处理

覆盖默认错误处理有两种方式

### 1) 参考源码完全自定义

源码位于: `NetConfig`

<img src="https://i.loli.net/2021/08/14/cTr57oulbhwxEaQ.png" width="400"/>

缺省页的错误处理是单独的: `onStateError`

### 2)  仅覆盖错误信息

默认错误处理的文本被定义在`strings.xml`中, 我们可以在项目中使用同名覆盖或者多语言
```xml
<!--网络请求异常-->
<string name="net_network_error">当前网络不可用</string>
<string name="net_url_error">请求资源地址错误</string>
<string name="net_host_error">无法找到指定服务器主机</string>
<string name="net_connect_timeout_error">连接服务器超时，%s</string>
<string name="net_download_error">下载过程发生错误</string>
<string name="net_no_cache_error">读取缓存错误</string>
<string name="net_parse_error">解析数据时发生异常</string>
<string name="net_request_error">请求参数错误</string>
<string name="net_server_error">服务响应错误</string>
<string name="net_null_error">数据为空</string>
<string name="net_error">未知网络错误</string>
<string name="net_other_error">未知错误</string>
<string name="no_error_message">无错误信息</string>
```