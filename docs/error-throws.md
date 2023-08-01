以下为Net内部抛出的异常对象

| 异常 | 描述 |
|-|-|
| NetException | 未知的网络异常, 一般情况应当继承他来创建自定义的网络异常  |
| HttpFailureException | Http请求错误, Http请求失败(onFailure) |
| HttpResponseException | Http请求成功后发生的错误, Http请求成功但发生异常(onResponse) |
| URLParseException | 地址错误 |
| NetUnknownHostException | 域名错误 |
| NetSocketTimeoutException | 连接超时 |
| NetConnectException | 连接网络失败, 设备未开启网络 |
| NetworkingException | 当前网络不可用, `Net并未实现` |
| DownloadFileException | 下载文件异常 |
| ConvertException | 解析错误, `NetConvert`中发生的未捕获异常都算解析错误 |
| RequestParamsException | 请求参数错误 `400 - 499` 范围内错误码 |
| ServerResponseException | 服务器错误 `500 - 599` 范围内错误码 |
| ResponseException | 错误码异常, 一般应用于后端返回的错误码和其定义的成功码不匹配 |
| NullPointerException | 空指针, 一般是在作用域内操作一个空的对象 |

!!! warning "NetworkingException"
    由于谷歌限制Net无法判断网络是否可用, 请开发者自己抛出

## 自定义异常

在转换器(NetConverter)或拦截器(Interceptor)中抛出任何异常
该异常对象都会被单例或全局错误处理接收, 可用于判断异常信息做特殊处理
(例如Token失效则跳转到登录页面)

!!! warning "转换发生异常"
    NetConverter中的所有异常除非是`NetException子类`否则都将被`ConvertException`包裹 <br>
    即捕获的是`ConvertException`, cause才为实际抛出异常

## 异常传递字段

Net自带异常会有类型为Any的字段`tag`, 可用传递对象用于判断错误处理

例如`ResponseException`常用于作为请求服务器成功但后端业务错误, 然后tag为传递的错误码

示例代码

=== "转换器抛出异常"

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

=== "全局错误处理异常"

    ```kotlin
    class NetworkingErrorHandler : NetErrorHandler {
        override fun onError(e: Throwable) {
        // .... 其他错误
            if (e is ResponseException && e.tag == 401) { // 判断异常为token失效
               // 打开登录界面或者弹登录失效对话框
            }
        }
    }
    ```