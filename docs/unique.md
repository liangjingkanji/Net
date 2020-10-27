某个重复发起的请求, 在发起的时候自动取消旧的网络请求. <br>

这种应用场景常见于筛选菜单, 每次点击菜单都会发起网络请求返回筛选后的列表, 但是请求未完成时, 用户又点击了新的筛选条件, 这个时候应该取消上次请求, 重新发起新的请求 <br>

这个需求在Net中非常好实现, 保存一个变量即可

```kotlin
var scope: AndroidScope? = null

btn_request.setOnClickListener {
    tv_result.text = "请求中"
    scope?.cancel() // 如果存在则取消

    scope = scopeNetLife {
        val result = Post<String>("api").await()
        Log.d("日志", "请求到结果") // 你一直重复点击"发起请求"按钮会发现永远无法拿到请求结果, 因为每次发起新的请求会取消未完成的
        tv_result.text = result
    }
}
```

当`scope`不为空时即表示存在上个请求, 我们无论上个请求是否完成都调用`cancel`函数保证取消即可

> 详细的关于取消网络请求的操作查看: [取消请求](cancel.md)