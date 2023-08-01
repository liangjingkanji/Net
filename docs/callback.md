Net支持OkHttp的原有的队列请求`Callback`

!!! Failure "不推荐"
    Callback属于接口回调, 其代码冗余, 且无法支持并发请求


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