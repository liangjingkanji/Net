# Net

异步任务库, Android 创新式的网络请求库(针对[Kalle](https://github.com/yanzhenjie/Kalle)网络请求框架进行扩展), 支持协程高并发网络请求



本项目为Android项目中的所有的异步任务和网络请求而生



1.0+ 版本为RxKotlin实现
2.0+ 版本开始引入Kotlin协程特性, 开发者无需掌握协程也可以使用, 两个版本存在Api变动需要手动迁移



主要新增特性

- 协程
- 并发网络请求
- 串行网络请求
- 切换线程
- DSL编程
- 方便的缓存处理
- 自动错误信息吐司
- 自动异常捕获
- 自动日志打印异常
- 自动JSON解析
- 自动处理下拉刷新和上拉加载
- 自动处理分页加载
- 自动缺省页
- 自动处理生命周期
- 自动处理加载对话框
- 协程作用域支持错误和结束回调



同时完全不影响[Kalle](https://github.com/yanzhenjie/Kalle)的特性

- 九种缓存模式
- 数据库缓存
- 缓存加密
- 上传进度监听
- 下载进度监听
- 断点续传
- 下载文件策略
- 网络连接判断
- 自定义数据转换器
- 网络拦截器
- 重定向
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
// 协程库
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0'

// 支持自动下拉刷新和缺省页的, 可选
implementation 'com.github.liangjingkanji:BRV:1.2.1'

implementation 'com.github.liangjingkanji:Net:2.0.5'
```





## 请求方式

请求方式支持同步和异步, 异步只允许在作用域内执行. 详情请看`Net.kt`文件

![image-20191223150901891](https://tva1.sinaimg.cn/large/006tNbRwgy1ga6o9s47lsj30dg0ca0tz.jpg)



请求方式全部属于协程中的`async`异步函数, 运行在IO线程中. 函数返回`Deferred<T>`对象, 该对象通过`await()`函数获取结果. 

执行await时会阻塞代码执行注意, 所以建议await在作用域最后一起执行, 保证请求全部发送出去然后统一获取结果.

### Post

```kotlin
scopeLife {

  val data = post<String>(
    "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
    absolutePath = true
  )

  textView.text = data.await()
}
```

`Model` 即JSONBean或者说POJO 数据模型, 会将服务器返回的JSON解析成该数据模型在`net`回调中可以使用`it`调用



### Get

```kotlin
scopeLife {

  val data = get<String>(
    "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
    absolutePath = true
  )

  textView.text = data.await()
}
```

`Model` 泛型如果换成String, 将会在成功回调中得到字符串对象.



### 文件上传

```kotlin
scopeLife {

  val data = post<String>(
    "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
    absolutePath = true
  ){
    file("file", File())
  }.await()

  textView.text = data.await()
}
```

这是支持Kalle任何参数添加方式



### 文件下载

```kotlin
scopeLife {
  download("/path", "下载目录"){

    // 进度监听
    onProgress { progress, byteCount, speed ->

               }

  }.await()
}
```



### 下载图片

下载图片要求首先导入Glide依赖库, 下载图片和下载文件不同在于可以手动指定图片宽高

```kotlin
Context.downloadImg(url: String, with: Int = -1, height: Int = -1)
```



示例

```kotlin
scopeLife {

  val data = downImage(
    "https://cdn.sspai.com/article/ebe361e4-c891-3afd-8680-e4bad609723e.jpg?imageMogr2/quality/95/thumbnail/!2880x620r/gravity/Center/crop/2880x620/interlace/1".
    200,200
  ).await()

}
```

## 初始化

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initNet("主机名"){

            // 转换器, 也可以自己实现Convert或者复写DefaultConverter.
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

## 

## 作用域

上面的网络请求方式全部使用的`scopeLife`, 该作用域属于跟随生命周期自动销毁作用域



所有作用域都会自动打印异常信息到LogCat, 作用域都是异步执行在主线程



### 异步作用域

该作用域的生命周期跟随整个应用, 不会自动取消, 需要你自己手动取消`cancel()`

```
scope {
	
}
```



### 生命周期作用域

该作用域默认在销毁`OnDestroy`时被销毁, 内部所有网络请求都会取消

```
scopeLife {
	
}
```

函数

```kotlin
fun LifecycleOwner.scopeLife(
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY, // 自定义销毁作用域的生命周期
    block: suspend CoroutineScope.() -> Unit
): AndroidScope
```



以上两种作用域不会自动吐司网络请求的异常信息

### 网络请求作用域

和异步作用域的区别就是会自动吐司网络请求的异常信息

```
scopeNet {

}
```



跟随生命周期的网络请求

```
scopeNetLife {
	
}
```



### 自动加载对话框

```
scopeDialog {

}
```

该作用域会在开始执行时显示对话框, 在作用域内全部任务执行完毕后取消对话框(异常或正常结束都会取消).

并且该对话框支持自定义全局或者通过函数参数传入单例对话框(仅该作用域对象使用的加载框)



> 自定义全局对话框

全局对话框设置通过NetConfig.onDialog设置

```kotlin
initNet("http://localhost.com") {
  onDialog {
    ProgressDialog(it).apply { setMessage("正在加载中") } // 返回一个Dialog
  }
}
```



### 自动缺省页



```
scopeState {

}
```



缺省页支持局部(使用控件对象)和全局页面(Activity或者Fragment对象)开启作用域

Fragment|Activity|View都支持直接通过`scopeState`函数开启作用域

StateLayout使用`scope`函数开启作用域



关于支持接收者类型有如下: 

- stateLayout
- activity
- fragment
- view

### 自动下拉刷新

兼具功能 下拉刷新|上拉加载|分页|缺省页

需要引入第三方库: [BRV](https://github.com/liangjingkanji/BRV)

```kotlin
pageRefreshLayout.onRefresh { 

  pageRefreshLayout.scope {

    val result = post<Model>("/path"){
      param("key", "value")
      param("page", index) // 页面索引使用pageRefreshLayout的属性index
    }

    val data = result.await().data

    if (data.isEmpty()){
      it.showEmpty()
      return
    }
    
    it.addData(data){
      index < data.totalPage // 判断是否存在下一页
    }
  }
}.showLoading
```

此时下拉和上拉都会调用该回调`onRefresh`中的接口请求. 



如果下拉刷新和上拉加载的接口不一致可以再实现`onLoadMore`回调

```kotlin
pageRefreshLayout.onLoadMore {
	// 上拉加载网络请求
}
```



showLoading属于现实缺省页中的加载页, 你也可以使用`autoRefresh()`显示下拉刷新的动画而不是缺省页



如果仅仅是自动完成下拉加载. 例如一般用户中心页面只需要自动处理下拉刷新的状态

```
pageRefreshLayout.scopeRefresh{

}
```



Tip: PageRefreshLayout只要加载成功后即使后续请求失败也不会显示错误缺省页

### 错误处理

所有作用域都支持`catch|finally`函数回调

```kotlin
scopeDialog {

  val data = get<String>(
    "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
    absolutePath = true
  )

  textView.text = data.await()
}.catch { 
	// 只有发生异常才会执行, it为异常对象
}.finally { 
 // 无论是否正常结束还是异常都会执行,  it为异常对象, 如果非异常结束为NULL
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



## 缓存和网络请求

很多App要求秒启动展示首页数据, 然后断网以后也可以展示缓存数据, 这种需求需要做到刷新UI数据两遍, 本框架同样方便实现



首先在初始化的时候启用缓存功能

```kotlin
initNet("http://localhost.com") {
	cacheEnabled()
}
```



可配置参数

```kotlin
fun KalleConfig.Builder.openCache(
    path: String = NetConfig.app.cacheDir.absolutePath, // 缓存保存位置, 默认应用缓存目录
    password: String = "cache" // 缓存密码, 默认cache
) 
```



发起缓存请求

```kotlin
scopeNetLife {

  Log.d("日志", "网络请求")

  val data = get<String>(
    "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
    CacheMode.NETWORK_YES_THEN_WRITE_CACHE,
    true
  )

  textView.text = data.await()

}.cache {

  Log.d("日志", "读取缓存")

  val data = get<String>(
    "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
    CacheMode.READ_CACHE,
    true
  )

  textView.text = data.await()
}
```

上面示例代码这种属于: 先加载缓存(没有缓存不会报异常), 后网络请求(缓存和网络请求都失败报异常信息), 网络请求成功缓存到本地并刷新界面UI.



注意事项

1. CacheMode: 属于Kalle的缓存模式, 共有九种缓存模式适用于不同的业务场景: [文档](https://yanzhenjie.com/Kalle/cache/)
2. cache: 该作用域内部允许抛出任何异常都不算错误, 这里的`cache`会比`scopeNetLife`先执行.
3. 当缓存读取成功视为作用域执行成功, 默认情况即使后续的网络请求失败也不会提示错误信息(cache函数参数指定true则提示)

## 轮循器

本框架附带一个超级强大的轮循器`Interval`, 基本上包含轮循器所需要到所有功能

- 支持多个观察者订阅同一个计时器
- 开始 | 暂停 | 继续 | 停止
- 同时包含RxJava的操作符`interval|intervelRange`功能
- 轮循器即Observable对象, 所以可以进行任意操作符转换. 例如轮循器控制网络请求



示例

```kotlin
Interval(1, TimeUnit.SECONDS).subscribe { 
	
}.finish { 
	
}.start()
```



函数

```
subscribe() // 即开启定时器, 订阅多个也会监听同一个计数器
start() // 开始
stop() // 结束
pause() // 暂停
resume() // 继续
reset // 重置轮循器 (包含计数器count和计时period) 不会停止轮循器
switch //  切换轮循器开关

state // 得到当前轮循器的状态
```
