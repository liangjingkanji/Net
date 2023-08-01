Net有完善的错误处理机制, 具备捕获异常/取消请求/错误提示/追踪链路

!!! success "收集网络日志"
    在Net作用域内发生的异常都会被全局错误处理捕获, 可以将其筛选上传日志

<br>
以下位置抛出异常会被捕获

| 函数 | 描述 |
|-|-|
| 作用域 | `scopeXX`代码块中 |
| 拦截器 | `Interceptor/RequestInterceptor` |
| 转换器 | `NetConverter`  |

如果捕获到错误默认会执行以下操作

1. `Logcat`输出异常堆栈信息, [自定义异常抛出](error-throws.md)
2. `Toast`显示错误文本, [自定义错误提示](error-tip.md)

<br>
!!! failure "捕获不到异常"
    如果请求未执行`await()`, 那么即使发生错误也不会被捕获到

<br>
自定义请阅读[全局错误处理](error-global.md)