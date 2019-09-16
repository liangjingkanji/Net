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
implementation 'com.github.liangjingkanji:Net:1.0'
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