## 3.0.20
1. 新增`HttpFailureException`及其子类表示请求失败异常
1. 新增`HttpResponseException`及其子类表示请求成功后发生的异常
1. 更新依赖库BRV至1.3.31

## 3.0.19
更新依赖库BRV至1.3.30

## 3.0.18
修复Response流异常关闭问题

## 3.0.17
修复Profiler导致的请求问题

## 3.0.16
日志记录器中请求参数默认使用URLDecoder解码

## 3.0.15
1. 修复FileProvider冲突
2. 上传文件时默认生成filename(当你未指定文件名情况下)
3. NetCallback网络请求被取消不会回调onError
4. 为NetCallback添加Request对象
5. 删除内部Tooltip依赖


## 3.0.14
1. 修复KType类型问题
2. 支持`File.install`函数安装应用

## 3.0.13
1. 优化标签相关函数. 整个网络生命周期可以完美传递参数
2. NetCallback具备跟随生命周期自动取消网络请求
3. 扩展NetCallback来添加DialogCallback/StateCallback/PageCallback
4. 废弃部分函数, 例如onDialog被废弃, 现在由`NetConfig.dialogFactory`构建全局加载对话框
5. initNet现在废弃, 由`NetConfig.init`取代. 所有配置信息由NetConfig承载
5. RequestParamsException添加错误码信息

## 3.0.12
修复转换器抛出的异常全部被`ConvertException`包裹的问题.

NetException子类不会被ConvertException包裹, 所以要捕获转换器中的自定义异常请其继承NetException

## 3.0.11
1. 修复临时下载文件错误问题
2. 修复ViewModel作用域复用问题

## 3.0.10
升级Tooltip依赖, 解决使用Tooltip_V1.1.1依赖时Net存在崩溃问题

## 3.0.9
1. 优化转换器异常处理
2. 转换器支持非固定格式Json解析

## 3.0.7
1. 修复未知的TypeToken访问权限问题

## 3.0.6
1. 所有Json解析框架都可以解析`List<UserModel>`等嵌套泛型数据结构: [特殊结构解析](convert-special.md)

## 3.0.5
1. 修复Path编码问题
2. 上传File自动识别MediaType

## 3.0.4
1. 添加MediaConst表示常用MediaType
2. OkHttpBuilder添加`setErrorHandler`取代onError/onStateError: [全局错误处理](error-handle.md)
3. 提供兼容Android4.4(API level 19)的版本: [Net-okhttp3](https://github.com/liangjingkanji/Net-okhttp3)

## 3.0.3
1. 添加`onResult`函数来执行队列请求: [队列请求](callback.md#onresult)
2. 添加`toResult`函数来执行同步请求: [同步请求](sync-request.md)
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