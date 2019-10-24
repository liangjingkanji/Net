# Net

针对[Kalle](https://github.com/yanzhenjie/Kalle)网络请求框架进行扩展



主要新增特性

- Kotlin DSL
- RxJava
- 自动错误信息吐司
- 自动JSON解析
- 自动处理下拉刷新和上拉加载
- 自动处理分页加载
- 自动缺省页
- 自动生命周期
- 自动处理加载对话框



同时完全不影响[Kalle](https://github.com/yanzhenjie/Kalle)的特性

- 九种缓存模式, 缓存加密
- 上传进度监听
- 下载进度监听
- 断点续传
- 下载文件策略
- 网络连接判断
- 自定义数据转换器
- 网络拦截器
- 连接重试
- 自定义请求体
- 全局配置
- Cookie
- SSH证书



## 安装

project of build.gradle

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```



module of build.gradle

```groovy
implementation 'com.github.liangjingkanji:Net:1.1.5'
```





## 初始化

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initNet("主机名"){

            // 替换默认的错误处理(可选)
            onError {

            }

            // 转换器, 也可以自己实现Convert或者复写DefaultConverter
            converter(object : DefaultConverter() {
                override fun <S> convert(succeed: Type, body: String): S? {
					// 解析Json, 我这里是用的Moshi, 你也可以使用Gson之类的解析框架
                    return Moshi.Builder().build().adapter<S>(succeed).fromJson(body)
                }
            })
        }
    }
}
```



### 错误信息

第一种覆盖`onError`函数



这里是默认的错误处理逻辑, 在初始化的时候也介绍过如何复写onError.

这里使用的系统默认的Toast进行错误信息提示用户, 你可以复写然后实现自己的吐司提示.

```kotlin
    var onError: Throwable.() -> Unit = {

        val message = when (this) {
            is NetworkError -> app.getString(R.string.network_error)
            is URLError -> app.getString(R.string.url_error)
            is HostError -> app.getString(R.string.host_error)
            is ConnectTimeoutError -> app.getString(R.string.connect_timeout_error)
            is ConnectException -> app.getString(R.string.connect_exception)
            is WriteException -> app.getString(R.string.write_exception)
            is ReadTimeoutError -> app.getString(R.string.read_timeout_error)
            is DownloadError -> app.getString(R.string.download_error)
            is NoCacheError -> app.getString(R.string.no_cache_error)
            is ParseError -> app.getString(R.string.parse_error)
            is ReadException -> app.getString(R.string.read_exception)
            is ResponseException -> msg
            else -> {
                printStackTrace()
                app.getString(R.string.other_error)
            }
        }

        Toast.makeText(app, message, Toast.LENGTH_SHORT).show()
    }
```



第二种复写`strings.xml`文件中的属性



复写同name的属性可以达到修改文本信息内容或者进行国际化

```xml
<resources>
    <string name="app_name">Net</string>


    <!--网络请求异常-->
    <string name="network_error">当前网络不可用</string>
    <string name="url_error">请求资源地址错误</string>
    <string name="host_error">无法找到指定服务器主机</string>
    <string name="connect_timeout_error">连接服务器超时，请重试</string>
    <string name="connect_exception">请检查网络连接</string>
    <string name="write_exception">发送数据错误</string>
    <string name="read_timeout_error">读取服务器数据超时，请检查网络</string>
    <string name="download_error">下载过程发生错误</string>
    <string name="no_cache_error">读取缓存错误</string>
    <string name="parse_error">解析数据时发生异常</string>
    <string name="read_exception">读取数据错误</string>
    <string name="other_error">服务器未响应</string>

    <!--DefaultConverter-->
    <string name="parse_data_error">解析数据错误</string>
    <string name="parse_json_error">解析JSON错误</string>
    <string name="request_error">请求参数错误</string>
    <string name="server_error">服务响应错误</string>


</resources>
```





## 请求方式

Post

```kotlin
post<Model>(""){
  param("key", "value")
}.net { 

}
```

`Model` 即JSONBean或者说POJO 数据模型, 会将服务器返回的JSON解析成该数据模型在`net`回调中可以使用`it`调用



Get

```
get<Model>(""){
  param("key", "value")
}.net { 

}
```

`Model` 泛型如果换成String, 将会在成功回调中得到字符串对象.



文件上传

```kotlin
post<Model>(""){
  file("file", File("path"))
}.net {

}
```

这是支持Kalle任何参数添加方式



文件下载

```kotlin
download("/path", "下载目录"){

  // 进度监听
  onProgress { progress, byteCount, speed ->

             }

}.dialog(this){

}
```



## 生命周期

将Fragment或者Activity作为参数传递即可在页面关闭时自动取消订阅, 避免操作已销毁视图.

```kotlin
post<Model>(""){
  param("key", "value")
}.net(activity) { 

}
```

其他的对话框或者缺省页和下拉刷新等自动支持生命周期管理

## 对话框

将会在网络请求开始时弹出对话框, 结束时关闭对话框.

```kotlin
post<Model>(""){
  file("file", File("path"))
}.dialog(this) {

}
```



自定义对话框

```kotlin
fun <M> Observable<M>.dialog(
    activity: FragmentActivity,
    dialog: Dialog = ProgressDialog(activity),
    cancelable: Boolean = true,
    block: (DialogObserver<M>.(M) -> Unit)? = null
)
```

- `cancelable` 决定对话框是否可以点击用户关闭
- `dialog` 传入自定义对话框



对话框关闭会导致网络请求被取消订阅

## 分页加载

需要引入第三方库: [BRV](https://github.com/liangjingkanji/BRV)



```kotlin
post<Model>(""){
  file("file", File("path"))
}.page(page) {

}
```



## 缺省页

需要引入第三方库: [StateLayout](https://github.com/liangjingkanji/StateLayout) (如果已经引入BRV可以不再引入)

```
post<Model>(""){
  param("key", "value")
}.state(stateLayout) { 

}
```



关于`state`函数支持参数类型有如下: 

- stateLayout
- activity
- fragment
- view



会根据参数的不同而给不同的对象添加缺省页状态

## 