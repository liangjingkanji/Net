本框架使用OkHttp作为请求内核, 使用Kotlin语言扩展OkHttp的函数设计

<br>
<p align="center"><strong>非常欢迎共同贡献代码</strong></p>
<br>

Net支持使用协程发起网络, 当然即使不会协程也可以使用该框架.

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
        val aData = aDeferred.await() // 等待任务A返回数据
        val bData = bDeferred.await() // 等待任务B返回数据
    }
    ```

多个网络请求放在同一个作用域内就可以统一控制, 如果你的多个网络请求毫无关联, 你可以创建多个作用域.

<br>

> 当`Get`或`Post`等函数调用就会开始发起网络请求, `await`只是等待其请求成功返回结果, 所以如果你在`await`后执行的网络请求,这不属于并发(属于串行)

并发的错误示例
```kotlin hl_lines="3"
scopeNetLife {
    // 请求A
    val aDeferred = Get<String>("http://www.baidu.com/").await()
    // 请求B, 由于上面使用`await()`函数, 所以必须等待A请求返回结果后才会执行B
    val bDeferred = Get<String>("http://www.baidu.com/")

    val bData = bDeferred.await() // 等待任务B返回数据
}
```


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
|`param`|支持基础类型/文件/RequestBody/Part|
|`json`|请求参数为JSONObject/JsonArray/String|
|`setQuery`|Url参数, 如果当前请求的Url请求则该函数等效于`param`函数|



### JSON请求体

这里提供三种创建Json请求的示例代码. 酌情选用

=== "JSON键值对(推荐)"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100)

    scopeNetLife {
        tv_fragment.text = Post<String>("api") {
            json("name" to name, "age" to age, "measurements" to measurements) // 同时也支持Map集合
        }.await()
    }
    ```

=== "JSONObject"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100)

    scopeNetLife {
        tv_fragment.text = Post<String>("api") {
            json(JSONObject().run {
                put("name", name)
                put("age", age)
                put("measurements", JSONArray(measurements))
            })
        }.await()
    }
    ```

=== "自定义的body"
    ```kotlin
    val name = "金城武"
    val age = 29
    val measurements = listOf(100, 100, 100)

    scopeNetLife {
        tv_fragment.text = Post<String>("api") {
            body(MyJsonBody(name, age, measurements))
        }.await()
    }
    ```

对于某些可能JSON请求参数存在固定值:

1. 可以考虑继承RequestBody来扩展出自己的新的Body(), 然后使用`param`函数传入请求中.
2. 定义拦截器


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

## 函数

即会在IO线程执行网络请求, 要求在协程作用域内执行

|请求函数|描述|
|-|-|
| [Get](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-get.md)|标准Http请求方法|
| [Post](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-post.md)|标准Http请求方法|
| [Head](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-head.md)|标准Http请求方法|
| [Options](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-options.md)|标准Http请求方法|
| [Trace](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-trace.md)|标准Http请求方法|
| [Delete](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-delete.md)|标准Http请求方法|
| [Put](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-put.md)|标准Http请求方法|
| [Patch](api/net/com.drake.net/kotlinx.coroutines.-coroutine-scope/-patch.md)|标准Http请求方法|