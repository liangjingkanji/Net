Net支持在当前线程执行, 会阻塞当前线程的同步请求 -- `execute`

这里介绍的是不使用协程的同步请求. 由于Android主线程不允许发起网络请求, 这里我们得随便创建一个子线程才可以开发起同步请求

=== "同步请求"

    ```kotlin
    thread {
        val result = Net.post("api").execute<String>() // 网络请求不允许在主线程
        tvFragment?.post {
            tvFragment?.text = result  // view要求在主线程更新
        }
    }
    ```
=== "toResult"

    ```kotlin
    thread {
        val result = Net.post("api").toResult<String>().getOrDefault("请求发生错误, 我这是默认值")
        tvFragment?.post {
            tvFragment?.text = result  // view要求在主线程更新
        }
    }
    ```

1. `execute`在请求发生错误时会抛出异常
2. `toResult`不会抛出异常, 通过`exception*`函数来获取异常信息, 且支持默认值等特性

> 同步请求应用场景一般是在拦截器中使用, 拦截器默认是子线程

作用域具体介绍可以看[创建作用域](scope.md)

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
