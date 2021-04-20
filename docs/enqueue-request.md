Net支持OkHttp的原有的队列请求 -- `enqueue`

> 队列属于接口回调请求, 由于其代码复杂, 非特殊情况下还是建议使用协程发起请求

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
        tv_fragment.text = data
    }
})
```

NetCallback相较于Callback的特性

1. 可以指定泛型数据转换
2. 新增三个回调函数: onSuccess/onFailure/onComplete
3. 以上三个回调函数都运行在主线程

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


