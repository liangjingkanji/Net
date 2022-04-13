Net支持OkHttp的原有的队列请求`Callback`

> Callback属于接口回调请求, 其代码冗余可读性不高, 并且无法支持并发请求协作


```kotlin
Net.post("api").enqueue(object : Callback {
    override fun onFailure(call: Call, e: IOException) {
    }

    override fun onResponse(call: Call, response: Response) {
        // 此处为子线程
        val body = response.body?.string() ?: "无数据"
        runMain {
            // 此处为主线程
            binding.tvFragment.text = body
        }
    }
})
```