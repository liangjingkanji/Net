常用于筛选列表请求, 选择新的筛选条件时应将上次未完成的取消后再发起请求

在Net禁止重复请求仅2行代码

```kotlin hl_lines="4"
var scope: AndroidScope? = null

btnFilter.setOnClickListener {
    scope?.cancel()

    scope = scopeNetLife {
        val result = Post<String>("api").await()
    }
}
```

当`scope`不为空时表示存在旧请求, 无论旧请求是否完成都可以调用`cancel()`保证取消即可

1.  [取消请求](cancel.md) <br>
2. 限制时间强制使用缓存, [配置缓存有效期](cache.md#_3)