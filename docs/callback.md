Net支持OkHttp的原有的队列请求 -- `Callback`

<p align="center">
<strong>如果你决定使用协程可以不必阅读本章</strong>
</p>

> Callback属于接口回调请求, 属于最原始的请求方式. 其代码冗余可读性不高, 并且无法支持并发请求协作

## Callback

Callback属于OkHttp最原始的请求回调接口

```kotlin
Net.post("api").enqueue(object : Callback {
    override fun onFailure(call: Call, e: IOException) {
    }

    override fun onResponse(call: Call, response: Response) {
    }
})
```

## NetCallback

原始的Callback是没有任何数据转换和全局错误功能的. 这里可以使用NetCallback

```kotlin
Net.post("api") {
    param("password", "Net123")
}.enqueue(object : NetCallback<String>() {
    override fun onSuccess(call: Call, data: String) {
        tvFragment.text = data
    }
})
```

NetCallback相较于Callback的特性

1. 可以指定泛型数据转换
2. 新增三个回调函数: onSuccess/onFailure/onComplete
3. 以上三个回调函数都运行在主线程

<br>
基本特性也被扩展在Callback中. 除开库本身自带的Callback你也可以仿照实现自己的特殊Callback

| 函数 | 描述 |
|-|-|
| DialogCallback | 自动显示隐藏加载对话框 |
| StateCallback | 自动显示缺省页 |
| PageCallback | 自动下拉刷新/上拉加载 |

## onResult

使用`onResult`可以更加灵活方便的处理队列请求. 使用Kotlin的空安全函数可以区分处理请求结果

```kotlin
Net.post("api").onResult<String> {

    getOrNull()?.let { // 如果成功就不为Null
        Log.d("日志", "请求成功")
        tvFragment.text = it
    }

    exceptionOrNull()?.apply {
        Log.d("日志", "请求失败")
        printStackTrace() // 如果发生错误就不为Null
    }

    Log.d("日志", "完成请求")
}
```

onResult也是在主线程中执行

|请求函数|描述|
|-|-|
|Net.get|标准Http请求方法|
|Net.post|标准Http请求方法|
|Net.head|标准Http请求方法|
|Net.options|标准Http请求方法|
|Net.trace|标准Http请求方法|
|Net.delete|标准Http请求方法|
|Net.put|标准Http请求方法|
|Net.patch|标准Http请求方法|


