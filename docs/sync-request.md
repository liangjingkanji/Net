Net支持在当前线程执行阻塞线程的同步请求

!!! question "什么是同步请求"
    即上个请求结束才会发起下个请求, 实际上协程也可以实现但是他不会阻塞线程

    同步请求应用场景一般是在拦截器(执行在子线程)中使用

因为Android主线程不允许发起网络请求, 这里创建一个子线程来演示

=== "返回数据"

    ```kotlin
    thread {
        val result = Net.post(Api.PATH).execute<String>() // 网络请求不允许在主线程
        tv?.post {
            tv?.text = result  // view要求在主线程更新
        }
    }
    ```

=== "返回Result"

    ```kotlin
    thread {
        val result = Net.post(Api.PATH).toResult<String>().getOrDefault("请求发生错误, 我是默认值")
        tv?.post {
            tv?.text = result  // view要求在主线程更新
        }
    }
    ```

1. `execute`在请求错误时会直接抛出异常
2. `toResult`不会抛出异常, 可`getOrThrow/exceptionOrNull`等返回异常对象





