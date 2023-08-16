!!! question "节流"
    在一定时间间隔内，只执行最后一次请求, 忽略其他多余的请求

搜索输入框一般都是输入完关键词后自动开始搜索

这个过程涉及到

1. 每次变化都搜索会导致服务器压力, 应在停止输入满足一定时间后自动搜索
2. 当产生新的搜索请求后应取消旧请求, 以防止旧数据覆盖新数据
3. 当输入内容没有变化(例复制粘贴重复内容到搜索框)不会发起搜索请求

<br>

<img src="https://i.loli.net/2021/08/14/DAhfwxa1NK4gbpq.gif" width="250"/>

<br>

```kotlin
var scope: CoroutineScope? = null

// distinctUntilChanged 表示过滤掉重复结果
binding.etInput.debounce().distinctUntilChanged().launchIn(this) {
    scope?.cancel() // 发起新的请求前取消旧的请求, 避免旧数据覆盖新数据
    scope = scopeNetLife { // 保存旧的请求到一个变量中
        tv.text = "请求中"
        tv.text = Get<String>(Api.TIME).await()
    }
}
```

指定参数设置节流阀超时时间
```kotlin
fun EditText.debounce(timeoutMillis: Long = 800)
```
<br>
1. [示例-自动搜索分页列表](https://github.com/liangjingkanji/Net/blob/a78d46118666a3509d3fcc79c1d28ad81e2d5a57/sample/src/main/java/com/drake/net/sample/ui/fragment/EditDebounceFragment.kt)