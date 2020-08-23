Net使用协程发起网络, 但是即使不会协程也可以使用该框架

<br>
这里演示发起网络`请求百度网站`内容的三个步骤

1. 创建作用域
1. 发起请求
1. 接收数据

=== "单个请求"
    ```kotlin
    scopeNetLife { // 创建作用域
        // 这个大括号内就属于作用域内部
        val data = Get<String>("http://www.baidu.com/").await() // 发起GET请求并返回`String`类型数据
    }
    ```
=== "串行请求"
    ```kotlin
    scopeNetLife {
        val data = Get<String>("http://0000www.baidu.com/").await() // 请求A 发起GET请求并返回数据
        val data = Get<String>("http://www.baidu.com/").await() // 请求B 将等待A请求完毕后发起GET请求并返回数据
    }
    ```
=== "并发请求"
    ```kotlin
    scopeNetLife {
        // 以下两个网络请求属于同时进行中
        val aDeferred = Get<String>("http://www.baidu.com/") // 发起GET请求并返回一个对象(Deferred)表示"任务A"
        val bDeferred = Get<String>("http://www.baidu.com/") // 发起请求并返回"任务B"

        // 随任务同时进行, 但是数据依然可以按序返回
        val aData = Deferred.await() // 等待任务A返回数据
        val bData = bDeferred.await() // 等待任务A返回数据
    }
    ```

多个网络请求放在同一个作用域内就可以统一控制, 如果你的多个网络请求毫无关联, 你可以创建多个作用域.

<br>
关于JSON解析以及全局URL等初始化配置后面讲解

## 请求参数
关于请求参数参数
```kotlin
scopeNetLife { // 创建作用域
    // 这个大括号内就属于作用域内部
    val data = Get<String>("http://www.baidu.com/"){
        param("u_name", "drake")
        param("pwd", "123456")
    }.await() // 发起GET请求并返回`String`类型数据
}
```

|请求函数|描述|
|-|-|
|`param`|请求体参数|
|`path`|路径|
|`urlParam`|Url参数|
|`file`|上传文件|
|`binary`|二进制|
|`binaries`|多个二进制|
|`body`|自定义请求体|


## RESTFUL
Net支持RestFul设计风格

```kotlin
private fun GET() {
    scopeNetLife {
        tv_fragment.text = Get<String>("http://www.baidu.com/").await()
    }
}

private fun POST() {
    scopeNetLife {
        tv_fragment.text = Post<String>("http://www.baidu.com/").await()
    }
}

private fun HEAD() {
    scopeNetLife {
        tv_fragment.text = Head<String>("http://www.baidu.com/").await()
    }
}

private fun PUT() {
    scopeNetLife {
        tv_fragment.text = Put<String>("http://www.baidu.com/").await()
    }
}

private fun PATCH() {
    scopeNetLife {
        tv_fragment.text = Patch<String>("http://www.baidu.com/").await()
    }
}

private fun DELETE() {
    scopeNetLife {
        tv_fragment.text = Delete<String>("http://www.baidu.com/").await()
    }
}

private fun TRACE() {
    scopeNetLife {
        tv_fragment.text = Trace<String>("http://www.baidu.com/").await()
    }
}

private fun OPTIONS() {
    scopeNetLife {
        tv_fragment.text = Options<String>("http://www.baidu.com/").await()
    }
}
```