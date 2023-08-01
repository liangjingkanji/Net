可实现`NetErrorHandler`接口来监听全局错误处理

=== "创建"
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
=== "配置"
    ```kotlin
    NetConfig.initialize(Api.HOST, this) {
        setErrorHandler(NetworkingErrorHandler))
    }
    ```

|NetErrorHandler|使用场景|触发位置|
|-|-|-|
|`onError`| 吐司错误信息 | `scopeNetLife/scopeDialog` |
|`onStateError` | 要求错误显示在缺省页 |`PageRefreshLayout.scope/StateLayout.scope`|


!!! warning "以下情况全局错误处理无效"

    1. 异步任务作用域(`scope/scopeLife`)发生的错误
    2. 使用[单例错误处理](error-single.md)处理的错误