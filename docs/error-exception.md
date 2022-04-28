以下为Net内部常见发生的异常对象. 不代表全部异常, 例如不包含开发者自己定义的异常


| 异常 | 描述 |
|-|-|
| NetException | 未知的网络异常, 一般情况应当继承他来创建自定义的网络异常  |
| HttpFailureException | Http请求错误, Http请求失败(onFailure) |
| HttpResponseException | Http请求成功后发生的错误, Http请求成功但发生异常(onResponse) |
| URLParseException | 地址错误 |
| NetUnknownHostException | 域名错误 |
| NetSocketTimeoutException | 连接超时 |
| NetConnectException | 连接网络失败, 设备未开启网络 |
| NetworkingException | 当前网络不可用, 设备网络异常 |
| DownloadFileException | 下载文件异常 |
| ConvertException | 解析错误, `NetConvert`中发生的未捕获异常都算解析错误 |
| RequestParamsException | 请求参数错误 `400 - 499` 范围内错误码 |
| ServerResponseException | 服务器错误 `500 - 599` 范围内错误码 |
| ResponseException | 错误码异常, 一般应用于后端返回的错误码和其定义的成功码不匹配 |
| NullPointerException | 空指针, 一般是在作用域内操作一个空的对象 |

> `NetConnectException/NetworkingException` 这两个异常一般是设备网络异常可以不用收集上报, 其他网络异常建议收集处理


## 自定义异常

我们可以在转换器(NetConverter)或者拦截器(Interceptor)中抛出任何异常对象, 比如token失效我们抛出一个自定义的`TokenException`异常对象. <br>
该异常对象在单例或全局错误处理都可以被收到. 可以用于判断是否为token失效(失效则跳转到登录页面)


> 转换器中发生的所有异常除非是NetException的子类否则都将被ConvertException包裹(即捕获的是ConvertException, cause才为实际抛出异常).

## 使用异常属性

Net自带的一些异常都会有一个类型为Any的属性`tag`. 可以用来传递任何对象来用于判断错误类型. 比如`ResponseException`我常用于作为请求服务器成功但是服务器业务错误. 然后tag为业务错误码

示例代码

在转换器中获取401

```kotlin
class SerializationConverter(
    val success: String = "0",
    val code: String = "code",
    val message: String = "msg"
) : NetConverter {

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        try {
            return NetConverter.onConvert<R>(succeed, response)
        } catch (e: ConvertException) {
            val code = response.code
            when {
                code in 200..299 -> { // 请求成功
                // ... 假设Token失效. 后端返回业务错误码 srvCode = 401
                    throw ResponseException(response, errorMessage, tag = srvCode) // 将业务错误码作为tag传递
                }
                code in 400..499 -> throw RequestParamsException(response, code.toString()) // 请求参数错误
                code >= 500 -> throw ServerResponseException(response, code.toString()) // 服务器异常错误
                else -> throw ConvertException(response)
            }
        }
    }
}
```

全局错误处理器

```kotlin
// 创建错误处理器
MyErrorHandler : NetErrorHandler {
    override fun onError(e: Throwable) {
    // .... 其他错误
        if (e is ResponseException && e.tag == 401) { // 判断异常为token失效
           // 打开登录界面或者弹登录失效对话框
        }
    }
}

// 初始化Net的时候设置错误处理器
NetConfig.initialize("host") {
    setErrorHandler(MyErrorHandler()
}
```