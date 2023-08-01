两种日志插件

| [Okhttp Profiler](https://github.com/itkacher/OkHttpProfiler) | [Profiler](https://developer.android.com/studio/profile/network-profiler?hl=zh-cn) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 列表显示                                                     | 动态曲线图                                                   |
| 要求添加`LogRecordInterceptor`                               | 可查看所有OkHttp的请求                                       |
| 原理是插件捕获LogCat日志, 线上环境请关闭                     | 无法捕获启动一瞬间的请求                                     |

## LogRecordInterceptor

添加日志拦截器

```kotlin hl_lines="2"
NetConfig.initialize(Api.HOST, this) {
    addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
}
```

| 构造参数 | 描述 |
|-|-|
| enabled | 是否启用日志 |
| requestByteCount | 请求日志信息最大字节数, 默认1MB |
| responseByteCount | 响应日志信息最大字节数, 默认4MB |

此时仅LogCat输出日志, 要预览请安装插件

## 安装插件

### 1. 安装插件
在插件市场搜索: "`Okhttp Profiler`"

<img src="https://i.loli.net/2021/08/14/pmld3qn49Xozvbx.png" width="100%"/>


### 2. 打开窗口
安装以后在AndroidStudio右下角打开窗口

<img src="https://i.loli.net/2021/08/14/p2r7o3tqlUSnCms.png" width="80%"/>

!!! warning "不显示日志"
    请在请求前确保有打开过插件窗口, 如果依然不显示可以反复打开/关闭窗口

    每次AS更新都需要该插件作者适配, 可能存在beta版本作者没有适配情况




使用效果

<img src="https://i.loli.net/2021/08/14/Uo9G3wXuv5VFTgn.png" width="100%"/>

| 标题 | 描述 |
|-|-|
| Device | 选择调试设备 |
| Process | 选择展示记录的进程 |
| <img src="https://i.loli.net/2021/08/14/QozLn48B12MI37E.png" width="10%"/> 抓取 | 一般情况不需要使用, 假设没有及时更新请点击图标 |
| <img src="https://i.loli.net/2021/08/14/hy8Kkwmpc5CGxlu.png" width="10%"/> 清空 | 清空记录 |


## 自定义日志

继承`LogRecordInterceptor`复写函数实现自定义

1. 复写`getRequestLog`返回解密请求参数
2. 复写`getResponseLog`返回解密响应参数

初始化时添加自己的拦截器

```kotlin
NetConfig.initialize(Api.HOST, this) {
    addInterceptor(MyLogRecordInterceptor(BuildConfig.DEBUG))
}
```

## 日志过滤

<img src="https://i.loli.net/2021/08/14/EG7yZYqa86ezMTC.png" width="350"/>

不想网络日志影响其他日志查看, 可以添加`OKPREL_`为日志折叠规则
<img src="https://i.loli.net/2021/08/14/KH3wcgk5AFYDeXd.png" width="100%"/>


## 其他网络框架

`LogRecordInterceptor`属于OkHttp拦截器, 其他网络请求框架也可以使用

甚至可以直接使用`LogRecorder`输出日志

| LogRecorder | 描述 |
|-|-|
| generateId | 产生一个唯一标识符, 用于判断为同一网络请求 |
| recordRequest | 记录请求信息 |
| recordResponse | 记录响应信息 |
| recordException | 记录请求异常信息 |