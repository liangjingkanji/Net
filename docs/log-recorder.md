一般网络请求都会选择在LogCat打印网络日志信息, 但LogCat日志可读性差, 甚至不完整

Net扩展`Okhttp Profiler`插件支持更好的网络日志输出, 支持加密的请求和响应信息

## 安装插件


### 1) 安装插件
在插件市场搜索: "`Okhttp Profiler`"

<img src="https://i.imgur.com/Pvncs1W.png" width="100%"/>


### 2) 打开窗口
安装以后在AndroidStudio右下角打开窗口

<img src="https://i.imgur.com/lZ0RvN4.png" width="80%"/>

> 请在每次使用前都先打开插件窗口, 如果有延迟或者不显示就反复打开下窗口


### 3) 初始化
```kotlin hl_lines="2"
initNet("http://github.com/") {
    addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
}
```

使用效果

<img src="https://i.imgur.com/PJsaKpx.png" width="100%"/>

| 标题 | 描述 |
|-|-|
| Device | 选择调试设备 |
| Process | 选择展示记录的进程 |
| <img src="https://i.imgur.com/bLXKLrI.png" width="10%"/> 抓取 | 一般情况不需要使用, 假设没有及时更新请点击图标 |
| <img src="https://i.imgur.com/WG2WgBy.png" width="10%"/> 清空 | 清空记录 |


<br>

## 单例禁用

```kotlin
scopeNetLife {
    tv_fragment.text = Get<String>("api") {
        setLogRecord(false) // 为当前请求禁用日志记录
    }.await()
}
```

## LogCat冗余日志过滤
实际上Net的网络日志还是会被打印到LogCat, 然后通过插件捕捉显示.

<img src="https://i.imgur.com/0BZAg4M.png" width="350"/>

如果不想LogCat的冗余日志影响查看其它日志, 可以通过AndroidStudio的功能折叠隐藏, 添加一个`OKPREL_`过滤字段即可
<img src="https://i.imgur.com/F6DoICr.png" width="100%"/>


## 扩展至其他请求框架

可能你项目中还残留其他网络框架, 也可以使用Net的日志记录器`LogRecorder`来为其他框架打印日志信息

| 函数 | 描述 |
|-|-|
| generateId | 产生一个唯一标识符, 用于判断为同一网络请求 |
| recordRequest | 记录请求信息 |
| recordResponse | 记录响应信息 |
| recordException | 记录请求异常信息 |