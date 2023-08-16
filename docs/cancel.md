部分场景开发者想手动取消请求

```kotlin
downloadScope = scopeNetLife {
    // 下载文件
    val file = Get<File>(Api.DOWNLOAD).await()
}

downloadScope.cancel() // 取消下载
```

## 任意位置取消
发起请求时指定`Id`

```kotlin
scopeNetLife {
    tv.text = Get<String>(Api.DOWNLOAD){
        setId("请求用户信息")
    }.await()
}
```

=== "根据ID取消"
    ``` kotlin
    Net.cancelId("请求用户信息")
    ```
=== "根据Group取消"
    ``` kotlin
    Net.cancelGroup("请求分组名称")
    ```

## Group和Id区别

| 函数 | 描述 |
|-|-|
| id | 请求唯一Id, 实际上重复也行, 但是取消请求时遍历到指定Id就会结束遍历 |
| group | 允许多个请求使用相同group, 在取消请求时会遍历所有分组的请求 <br>  |

!!! warning "作用域结束请求自动取消"
    在`scopeXX()`作用域中发起请求时会默认使用当前协程错误处理器作为Group
    ```kotlin
    setGroup(coroutineContext[CoroutineExceptionHandler])
    ```
    在作用域结束时 会`cancelGroup`, 所以如果你手动指定分组会导致无法自动取消