Net具备完善的错误处理机制, 能捕获大部分网络请求或者异步任务导致的崩溃, 减少App崩溃和收集详细的错误信息

<br>
以下场景的抛出的异常会被Net捕获到(不会导致崩溃)

1. 作用域内部 (scope**等函数大括号`{}`内部)
1. 拦截器中 (Interceptor/RequestInterceptor)
1. 转换器中 (NetConverter)


<br>

如果捕获到错误默认会执行以下操作

-  `Logcat`中会输出详细的异常堆栈信息, 如果想要输出更详细内容请阅读[自定义异常](error-exception.md)
-  `Toast`吐司错误异常信息, 如果想要自定义或者国际化错误文本请阅读[自定义错误提示](error-tip.md)

<br>
要改变以上的默认错误处理请阅读阅读[全局错误处理](error-global.md), 默认全局错误处理器实现源码: [NetErrorHandler](https://github.com/liangjingkanji/Net/blob/97c31dddde7ced5aa75411d2581c858ca494669e/net/src/main/java/com/drake/net/interfaces/NetErrorHandler.kt#L18) <br>


> 建议在全局错误处理器中将捕获到的Exception(除无网络异常意外)上报到崩溃统计平台







