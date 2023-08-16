捕获当前请求/作用域错误, 将不会再被全局错误处理

## 捕获请求

一个请求发生错误会取消当前作用域内所有请求, 但单独捕获后不会再影响其他请求

例如
```kotlin
scopeNetLife {
    Get<String>("path").await() // 失败
    Get<String>("path2").await() // 上面失败, 此处也不会执行
}
```

捕获第一个协程避免终止后续执行
```kotlin
scopeNetLife {
    try {
        Get<String>("path").await() // 失败
    } catch(e:Exception) {
    }
    Get<String>("path2").await() // 上面失败, 此处继续执行
}
```
当然如果创建不同的作用域分别请求那是互不影响的
```kotlin
scopeNetLife {
    Get<String>("path").await() // 失败
}
scopeNetLife {
    Get<String>("path2").await() // 上面失败, 此处完全不受影响
}
```

<br>

## 捕获作用域

```kotlin
scope {
    val data = Get<String>("http://www.error.com/").await()
}.catch {
    // 协程内发生错误回调, it为异常对象
}.finally {
    // 协程执行完毕回调(不论成败), it为异常对象
}
```

| 函数 | 区别 |
|-|-|
| catch | 发生错误后回调, 取消不回调 <br>不会再执行全局错误处理, 可使用`handleError`再次传递给全局 |
| finally | 执行完毕回调(不论成败), 取消作用域时`it`为`CancellationException` <br>执行全局错误处理 |