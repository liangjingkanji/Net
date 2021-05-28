## 3.0.5
1. 修复Path编码问
2. 上传File自动识别MediaType

## 3.0.4
1. 添加MediaConst表示常用MediaType
2. OkHttpBuilder添加`setErrorHandler`取代onError/onStateError: [全局错误处理](error-handle.md)
3. 提供兼容Android4.4(API level 19)的版本: [Net-okhttp3](https://github.com/liangjingkanji/Net-okhttp3)

## 3.0.3
1. 添加`onResult`函数来执行队列请求: [队列请求](../enqueue-request/#-result)
2. 添加`toResult`函数来执行同步请求: [同步请求](../sync-request/)
3. 请求体日志支持JSON/文本类型
4. LogRecordInterceptor暴露`requestString/responseString`实现函数用于继承实现自定义需求


## 3.0.2
修复多渠道无法安装问题

## 3.0.1
1. 转换器支持KType, 解决JAVA泛型擦除问题
2. 支持kotlin-serialization转换器
3. 修复JSON请求参数类型问题

## 3.0
1. 重构代码, 提升稳定性和安全性.
2. 支持OkHttp的所有函数/组件, 可独立升级OkHttp版本
3. 更加强大的下载功能
4. 所有IO读写使用OKIO
5. 更加优雅的函数设计