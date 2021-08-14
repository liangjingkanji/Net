一般网络请求都会选择在LogCat打印网络日志信息, 但LogCat日志可读性差, 且不完整

Net扩展[Okhttp Profiler](https://github.com/itkacher/OkHttpProfiler)插件以支持更好的网络日志输出

## 添加日志拦截器

```kotlin hl_lines="2"
NetConfig.init("http://github.com/") {
    addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
}
```

| 构造参数 | 描述 |
|-|-|
| enabled | 是否启用日志 |
| requestByteCount | 请求日志信息最大字节数, 默认1MB |
| responseByteCount | 响应日志信息最大字节数, 默认4MB |

这样会可以在LogCat看到日志输出, 但是我们要使用插件预览就需要第 2 步

## 安装插件

### 1. 安装插件
在插件市场搜索: "`Okhttp Profiler`"

<img src="https://i.loli.net/2021/08/14/pmld3qn49Xozvbx.png" width="100%"/>


### 2. 打开窗口
安装以后在AndroidStudio右下角打开窗口

<img src="https://i.loli.net/2021/08/14/p2r7o3tqlUSnCms.png" width="80%"/>

> 请在每次使用前都先打开插件窗口, 如果有延迟或者不显示就反复打开下窗口




使用效果

<img src="https://i.loli.net/2021/08/14/Uo9G3wXuv5VFTgn.png" width="100%"/>

| 标题 | 描述 |
|-|-|
| Device | 选择调试设备 |
| Process | 选择展示记录的进程 |
| <img src="https://i.loli.net/2021/08/14/QozLn48B12MI37E.png" width="10%"/> 抓取 | 一般情况不需要使用, 假设没有及时更新请点击图标 |
| <img src="https://i.loli.net/2021/08/14/hy8Kkwmpc5CGxlu.png" width="10%"/> 清空 | 清空记录 |


<br>

## 单例禁用日志

```kotlin
scopeNetLife {
    tvFragment.text = Get<String>("api") {
        setLogRecord(false) // 为当前请求禁用日志记录
    }.await()
}
```

## 自定义日志(解密)

通过继承`LogRecordInterceptor`可以覆写函数自定义自己的日志输出逻辑

1. 如果你的请求体是被加密的内容, 你可以通过覆写`requestString`函数返回解密后的请求信息
2. 如果你的响应体是被加密的内容, 你可以通过覆写`responseString`函数返回解密后的响应信息

然后初始化时添加自己实现拦截器即可

```kotlin
NetConfig.init("http://github.com/") {
    addInterceptor(MyLogRecordInterceptor(BuildConfig.DEBUG))
}
```

## LogCat过滤
实际上Net的网络日志还是会被打印到LogCat, 然后通过插件捕捉显示.

<img src="https://i.loli.net/2021/08/14/EG7yZYqa86ezMTC.png" width="350"/>

如果不想LogCat的冗余日志影响查看其它日志, 可以通过AndroidStudio的功能折叠隐藏, 添加一个`OKPREL_`过滤字段即可
<img src="https://i.loli.net/2021/08/14/KH3wcgk5AFYDeXd.png" width="100%"/>


## 其他网络框架

可能你项目中还残留其他网络框架, 也可以使用Net的日志记录器`LogRecorder`来为其他框架打印日志信息

| 函数 | 描述 |
|-|-|
| generateId | 产生一个唯一标识符, 用于判断为同一网络请求 |
| recordRequest | 记录请求信息 |
| recordResponse | 记录响应信息 |
| recordException | 记录请求异常信息 |