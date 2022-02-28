单例捕获即只捕获某个作用域或者接口, 不会影响到全局

- 捕获请求
- 捕获作用域

<br>

## 捕获请求

一个作用域内常常有多个请求发生. 默认情况下一个请求发生错误就会取消当前作用域内部所有协程, 这个时候我们可以捕获错误请求来进行其他处理

例如
```kotlin
scopeNetLife {
    Get<String>("api").await() // 失败
    Get<String>("api2").await() // 上面失败, 此处也不会执行
}
```

捕获第一个协程避免终止后续执行
```kotlin
scopeNetLife {
    try {
        Get<String>("api").await() // 失败
    } catch(e:Exception) {
    }
    Get<String>("api2").await() // 上面失败, 此处继续执行
}
```
当然如果你创建不同的作用域分别请求那是互不影响的
```kotlin
scopeNetLife {
    Get<String>("api").await() // 失败
}
scopeNetLife {
    Get<String>("api2").await() // 上面失败, 此处完全不受影响
}
```

<br>

## 捕获作用域

```kotlin
scope {
    val data = Get<String>("http://www.thisiserror.com/").await()
}.catch {
    // 协程内部发生错误回调, it为异常
}.finally {
    // 协程内协程全部执行完成, it为异常(如果是正常结束则it为null)
}
```

以下函数幕后字段`it`为异常对象, 如果正常完成it则为null. 如果属于请求被手动取消则it为`CancellationException`

| 函数 | 描述 |
|-|-|
| catch | 作用域被`catch`则不会被传递到全局异常处理回调中: [全局处理异常](exception-handle.md), 除非使用`handleError`再次传递给全局 |
| finally | 同样可以获取到异常对象, 且不影响全局异常回调处理 |