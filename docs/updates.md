## 3.4.5
- Fixed [#88](https://github.com/liangjingkanji/Net/issues/80)

## 3.4.4
- 新增Cookie管理

## 3.4.3
- 修复gzip导致的强制缓存读取失败
- 修复NoCacheException无法捕获
- 响应可以判断是否来自于缓存
- 优化缓存相关代码

## 3.4.2
- 修复Http缓存协议无效
- 日志拦截器错误信息简略

## 3.4.1
- 禁用失败缓存

## 3.4.0
- 新增[强制缓存模式](cache.md)
- 新增预览模式(可实现缓存+网络)

## 3.3.1
废弃部分日志相关函数
新增Net.debug日志输出函数
修复Query编码问题

## 3.3.0
- 删除Callback/onResult(破坏性迁移)
- 删除requestById/requestByGroup(破坏性迁移)
- Add View.scopeNetLife
- 网络请求异常位置追踪

## 3.2.2
- Fixed [#77](https://github.com/liangjingkanji/Net/issues/77)
- 修复gzip启用情况下载进度始终为0
- `Progress.finish()`方法改为属性

## 3.2.1
- Fixed [#76](https://github.com/liangjingkanji/Net/issues/76)
- `RequestBody?.peekString()`函数接受者改为非可空类型

## 3.2.0
- 更改tag相关函数
- 更改`NetConfig.app`类型改为Context
- 删除敏感权限(外部存储读写)
- 删除废弃函数/提高函数废弃等级
- 废弃`NetConfig.init`
- 删除fastest函数接受者
- 删除logRecord属性
- 优化域名解析错误异常信息
- 新增无网络不可用异常`NetworkingException`
- 新增输出MultiPart参数日志
- 更改RetryInterceptor/LogRecordInterceptor属性访问权限

## 3.1.2
- 升级BRV至1.3.51, [其内部升级SmartRefreshLayout至2.0.5](https://github.com/liangjingkanji/BRV/issues/85). SmartRefreshLayout相关依赖需要迁移
- Interval添加`onlyResumed`函数
- Interval修改`life`函数参数为FragmentActivity情况下的自动取消轮询器的生命周期

## 3.1.1
内嵌混淆规则, 不需要手动添加

## 3.1.0
- 修复部分子线程开启作用域崩溃
- 网络异常堆栈使用 NetConfig.TAG 作为标签, 使用debug日志类型输出
- 默认转换器现在要求HTTP状态码为成功才返回数据(泛型Response请求完成都返回)
- 开启编译器强制替换废弃函数(可查看函数注释替换规则)
- 禁止暴露冗余函数

## 3.0.27
- setDownloadDir函数同时支持完整路径(即包含文件名称)和下载目录

## 3.0.26
- 优化返回ByteArray类型性能
- setQuery函数支持Number/Boolean类型
- 新增一个错误提示 HttpFailureException


## 3.0.25
- 轮询器支持ViewModel取消
- 修复无法返回ByteArray类型问题
- 间接依赖 update brv 1.3.37
- 更新函数注释

## 3.0.24
修复上传文件包含参数时, 编码问题导致的参数错误

## 3.0.23
1. 修复轮询器重复start无效问题
2. Interval其他优化

## 3.0.21
Interval添加cancel函数用于取消计时器, 取消完成不会调用finish

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
5. initNet现在废弃, 由`NetConfig.initialize`取代. 所有配置信息由NetConfig承载
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