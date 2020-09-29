一般网络请求都会选择在LogCat打印网络日志信息, 但是AndroidStudio的LogCat超长文本会被切割, 甚至发生不完整的情况, 而且容易和其他日志掺杂, 导致可读性差.

Net扩展`Okhttp Profiler`插件支持更好的日志拦截信息, 支持加密的请求和响应信息

## 安装插件

在插件市场搜索: "`Okhttp Profiler`"

<img src="https://i.imgur.com/Pvncs1W.png" width="100%"/>

### 初始化
```kotlin hl_lines="3"
initNet("http://182.92.97.186/") {
    converter(JsonConvert()) // 转换器
    setLogRecord(true) // 开启日志记录功能
}
```

使用效果

<img src="https://i.imgur.com/PJsaKpx.png" width="100%"/>

| 标题 | 描述 |
|-|-|
| Device | 选择调试设备 |
| Process | 选择展示记录的进程 |
| <img src="https://i.imgur.com/bLXKLrI.png" width="20%"/> 抓取 | 一般情况不需要使用, 假设没有及时更新请点击图标 |
| <img src="https://i.imgur.com/WG2WgBy.png" width="20%"/> 清空 | 清空记录 |


### 自定义转换器

假设你使用的是`DefaultConvert`或者没有使用转换器直接返回String则无需多余处理, 如果覆写或者直接实现的`Convert`, 请确保`result.logResponseBody`被赋值
```kotlin hl_lines="16"
@Suppress("UNCHECKED_CAST")
abstract class DefaultConvert(
    val success: String = "0",
    val code: String = "code",
    val message: String = "msg"
) : Converter {

    override fun <S, F> convert(
        succeed: Type,
        failed: Type,
        request: Request,
        response: Response,
        result: Result<S, F>
    ) {
        val body = response.body().string()
        result.logResponseBody = body // 将字符串响应赋值给result.logResponseBody
        // .... 其他操作
    }
}
```
<br>

!!! note
    假设后端返回的加密数据, 可以为`result.logResponseBody`赋值解密后的字符串


### 请求参数加密

假设请求参数为加密后的字符串请在拦截器中为日志记录器赋值请求参数字符串

```kotlin hl_lines="5"
class NetInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()

        request.logRequestBody("解密后的请求参数字符串")

        return chain.proceed(request)
    }
}
```

<br>

!!! note
    实际上Net的网络日志还是会被打印到LogCat, 然后通过插件捕捉显示. 如果不想LogCat的冗余日志影响查看其它日志, 可以通过AndroidStudio的功能折叠隐藏
    <img src="https://i.imgur.com/F6DoICr.png" width="100%"/>


## 扩展至其他请求框架

可能你项目中还残留其他网络框架, 也可以使用Net的日志记录器`LogRecorder`来为其他框架打印日志信息

| 函数 | 描述 |
|-|-|
| generateId | 产生一个唯一标识符, 用于判断为同一网络请求 |
| recordRequest | 记录请求信息 |
| recordResponse | 记录响应信息 |
| recordException | 记录请求异常信息 |
| recordDuration | 记录请求间隔时间 |