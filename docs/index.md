本框架使用Android主流的Http框架OkHttp作为请求内核, 遵守不影响OkHttp原有的函数组件使用原则开发

<br>
<p align="center"><strong>非常欢迎共同贡献代码/修订文档, 点击文档右上角小铅笔可直接修订文档 ↗</strong></p>
<br>


## 前言

1. 避免写一个函数封装一个请求函数, 禁止俄罗斯套娃 <br>
2. Post/Get等函数本身就是高扩展性的`请求动作`. 你有特殊请求可以在拦截器/转换器处理, 或者使用TAG标签区分请求 <br>
3. 如果想快速改变项目全局的请求URL可以声明为全局常量
4. Net支持使用协程发起网络, 当然即使不会协程也可以使用该框架.
5. 如果你觉得文档看不懂或者有歧义那肯定是作者问题, 请反馈给作者或者自我修订


## 使用

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

## RESTFUL
Net支持RestFul设计风格

```kotlin
scopeNetLife {
    tv_fragment.text = Get<String>("http://www.baidu.com/").await()
    tv_fragment.text = Post<String>("http://www.baidu.com/").await()
    tv_fragment.text = Head<String>("http://www.baidu.com/").await()
    tv_fragment.text = Put<String>("http://www.baidu.com/").await()
    tv_fragment.text = Patch<String>("http://www.baidu.com/").await()
    tv_fragment.text = Delete<String>("http://www.baidu.com/").await()
    tv_fragment.text = Trace<String>("http://www.baidu.com/").await()
    tv_fragment.text = Options<String>("http://www.baidu.com/").await()
}
```

## 函数

默认在IO线程执行网络请求(通过作用域参数可以控制Dispatch调度器), 要求在协程作用域内执行.

|请求函数|描述|
|-|-|
| [Get](api/-net/com.drake.net/-get.html)|标准Http请求方法|
| [Post](api/-net/com.drake.net/-post.html)|标准Http请求方法|
| [Head](api/-net/com.drake.net/-head.html)|标准Http请求方法|
| [Options](api/-net/com.drake.net/-options.html)|标准Http请求方法|
| [Trace](api/-net/com.drake.net/-trace.html)|标准Http请求方法|
| [Delete](api/-net/com.drake.net/-delete.html)|标准Http请求方法|
| [Put](api/-net/com.drake.net/-put.html)|标准Http请求方法|
| [Patch](api/-net/com.drake.net/-patch.html)|标准Http请求方法|