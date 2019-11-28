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

project 的 build.gradle

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```



module 的 build.gradle

```groovy
implementation 'com.github.liangjingkanji:Net:1.2.8'
```







## 初始化和配置

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initNet("主机名"){

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
internal var onError: Throwable.() -> Unit = {

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
    is ReadException -> app.getString(R.string.read_exception)
    is ParseError -> app.getString(R.string.parse_error)
    is ParseJsonException -> app.getString(R.string.parse_json_error)
    is RequestParamsException -> app.getString(R.string.request_error)
    is ServerResponseException -> app.getString(R.string.server_error)
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
    <string name="parse_json_error">解析JSON错误</string>
    <string name="request_error">请求参数错误</string>
    <string name="server_error">服务响应错误</string>
    <string name="other_error">服务器未响应</string>

</resources>
```



### 初始化配置

在初始化的时候可以选择配置网络请求

```kotlin
initNet("http://192.168.2.1") {

  // 默认错误处理
  onError {

  }

  // PageObserver 默认错误处理
  onPageError {

  }

  // 默认加载对话框
  onDialog {

    ProgressDialog(it)
  }

  converter(object : DefaultConverter() {
    override fun <S> convert(succeed: Type, body: String): S? {
      return Moshi.Builder().build().adapter<S>(succeed).fromJson(body)
    }
  })
}
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

Net框架本身的生命周期管理是针对Fragment|Activity的页面销毁时才会自动取消订阅, 如果需要自定义页面生命周期推荐使用我的另外一个库: [AutoDispose](https://github.com/liangjingkanji/AutoDispose)

返回值 Disposable 可以用于完全手动任何位置取消, 

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
pageRefreshLayout.onRefresh { 
    post<Model>("/path"){
        param("key", "value")
        param("page", index) // 页面索引使用pageRefreshLayout的属性index
    }.page(page) {
        if (it.data.isEmpty()){
            showEmpty()
            return
        }
        addData(it.data){
            index < it.data.totalPage // 判断是否存在下一页
        }
    } 
}
```

此时下拉和上拉都会调用该回调`onRefresh`中的接口请求. 

如果下拉刷新和上拉加载的接口不一致可以再实现`onLoadMore`回调

```kotlin
pageRefreshLayout.onLoadMore {
	// 上拉加载网络请求
}
```





某些情况存在一些页面仅仅需要下拉刷新, 不需要分页/缺省页/上拉加载, 例如用户中心的刷新. 这个时候我们应该使用`refresh`函数而不是`page`.

```
post<Model>(""){
  param("key", "value")
}.refresh(smartRefreshLayout) {

}
```



refresh函数

```kotlin
/**
 * 自动结束下拉加载
 * @receiver Observable<M>
 * @param pageRefreshLayout SmartRefreshLayout
 * @param loadMore 是否启用上拉加载
 * @param block (M) -> UnitUtils
 */
fun <M> Observable<M>.refresh(
    pageRefreshLayout: PageRefreshLayout,
    loadMore: Boolean = false,
    block: RefreshObserver<M>.(M) -> Unit
) {
    subscribe(object : RefreshObserver<M>(pageRefreshLayout, loadMore) {
        override fun onNext(t: M) {
            block(t)
        }
    })
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

##Observer

无论是`page/refresh/net/dialog`这些函数本身都是快速创建Observer的扩展函数而已, 如果你需要拿到Observer的onError/onCompleted等回调请自己创建匿名类或者继承.



例如查看page源码即可看到只是创建一个Observer订阅而已

```kotlin
fun <M> Observable<M>.page(
    pageRefreshLayout: PageRefreshLayout,
    block: PageObserver<M>.(M) -> Unit
) {
    subscribe(object : PageObserver<M>(pageRefreshLayout) {
        override fun onNext(t: M) {
            block(t)
        }
    })
}
```

所有扩展订阅函数的都在`ObserverUtils`类中



扩展函数都会返回对应的Observer对象可以进行手动取消订阅等操作

```kotlin
val netObserver = download(
    "https://cdn.sspai.com/article/ebe361e4-c891-3afd-8680-e4bad609723e.jpg?imageMogr2/quality/95/thumbnail/!2880x620r/gravity/Center/crop/2880x620/interlace/1",
    isAbsolutePath = true
).net(this) {

}.error {
    // 自定义自己的错误处理
    handleError(it) // 该函数每个Observer都存在, 属于默认的错误处理操作
}
```



## 请求和响应规范

很多时候存在请求和响应的后台接口规范不是常规统一的, 这个时候我们可以自己拦截处理数据. 

主要是实现拦截器(`Interceptor`)和转换器(`Convert`)



Interceptor 这个和Okhttp同样, 可以拦截和修改请求参数, 并且可以获得Response的实例.

### Interceptor

这里可以得到Request和Response, 进行数据添加修改以及重新拼装请求, 不熟悉的请搜索Okhttp Interceptor使用.

### Convert

Convert 主要进行数据转换, 这里一般解析JSON对象.



这就是实现`DefaultConvert`自己解析Json对象. DefaultConverter是框架中定义的一个默认处理JSON示例, 一般情况使用它解析下JSON即可. 

```kotlin
initNet("http://localhost.com") {
    converter(object : DefaultConverter() {
        override fun <S> convert(succeed: Type, body: String): S? {
            return Moshi.Builder().build().adapter<S>(succeed).fromJson(body)
        }
    })
}
```

DefaultConvert构造函数拥有三个参数默认值

```kotlin
abstract class DefaultConverter(
    val successCode: String = "0",
    val codeName: String = "code",
    val msgName: String = "msg"
)
```

因为内部需要得到错误码`codeName`来判断请求是否真正成功以及错误消息`msgName`来在错误的时候进行打印吐司错误信息. 

所以需要知道解析JSON时的Key来获取.



如果涉及到响应的JSON数据需要解密或者错误码为某个数字时跳转登录界面可以直接重写`Convert`(建议复制参考DefaultConvert源码). 



## 轮循器

本框架附带一个轮循器`Interval`

- 支持多个观察者订阅同一个计时器
- 开始(即subscribe订阅) | 暂停 | 继续 | 停止
- 同时包含RxJava的操作符`interval|intervelRange`功能
- 轮循器即Observable对象, 所以可以进行任意操作符转换. 例如轮循器控制网络请求



函数

```
subscribe() // 即开启定时器, 订阅多个也会监听同一个计数器
stop() // 结束
pause() // 暂停
resume() // 继续
```