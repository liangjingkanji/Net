本框架使用Android主流的Http框架OkHttp作为请求内核, 遵守不影响OkHttp原有的函数组件使用原则开发

<br>
<p align="center"><strong>非常欢迎共同贡献代码/修订文档, 点击文档右上角小铅笔可直接修订文档 ↗</strong></p>
<br>


## 前言

1. 建议创建一个Api.kt的`object`单例类存储所有请求路径常量
1. `Post/Get等`函数属于请求动作. `scope*`等函数属于作用域, 假设你有某个请求需要重复使用建议封装`请求动作`而不是作用域
1. Net使用目前最先进的协程并发网络, 不会协程也可以使用本框架
1. 如果你觉得文档看不懂或者有歧义那肯定是作者问题, 请反馈给作者或者自我修订


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
        val data = Get<String>("https://github.com/liangjingkanji/Net/").await() // 发起GET请求并返回`String`类型数据
    }
    ```
=== "串行请求"
    ```kotlin
    scopeNetLife {
        val data = Get<String>("http://www.baidu.com/").await() // 请求A 发起GET请求并返回数据
        val data = Get<String>("https://github.com/liangjingkanji/Net/").await() // 请求B 将等待A请求完毕后发起GET请求并返回数据
    }
    ```
=== "并发请求"
    ```kotlin
    scopeNetLife {
        // 以下两个网络请求属于同时进行中
        val aDeferred = Get<String>("https://github.com/liangjingkanji/Net/") // 发起GET请求并返回一个对象(Deferred)表示"任务A"
        val bDeferred = Get<String>("https://github.com/liangjingkanji/Net/") // 发起请求并返回"任务B"

        // 随任务同时进行, 但是数据依然可以按序返回
        val aData = aDeferred.await() // 等待任务A返回数据
        val bData = bDeferred.await() // 等待任务B返回数据
    }
    ```

多个网络请求放在同一个作用域内就可以统一控制, 如果你的多个网络请求毫无关联, 你可以创建多个作用域.

> 多进程或Xposed项目要求[初始化](config.md/#_1)

<br>

> 当`Get`或`Post`等函数调用就会开始发起网络请求, `await`只是等待其请求成功返回结果, 所以如果你在`await`后执行的网络请求,这不属于并发(属于串行)

并发的错误示例
```kotlin hl_lines="3"
scopeNetLife {
    // 请求A
    val aDeferred = Get<String>("https://github.com/liangjingkanji/Net/").await()
    // 请求B, 由于上面使用`await()`函数, 所以必须等待A请求返回结果后才会执行B
    val bDeferred = Get<String>("https://github.com/liangjingkanji/Net/")

    val bData = bDeferred.await() // 等待任务B返回数据
}
```

## 返回结果

返回结果支持的数据类型(即Post等函数的`泛型`)由转换器决定, 默认支持以下类型

| 函数 | 描述 |
|-|-|
| String | 字符串 |
| ByteArray | 字节数组 |
| ByteString | 内部定义的一种字符串对象 |
| Response | 最基础的响应 |
| File | 文件对象, 这种情况其实应当称为[下载文件](download-file.md) |

非以上类型要求[自定义转换器](converter.md)

> 转换器的返回值决定你的网络请求的返回结果类型, 你甚至可以返回null, 前提是泛型为可空类型


## RestFul
Net支持RestFul设计风格

```kotlin
scopeNetLife {
    tvFragment.text = Get<String>("https://github.com/liangjingkanji/Net/").await()
    tvFragment.text = Post<String>("https://github.com/liangjingkanji/Net/").await()
    tvFragment.text = Head<String>("https://github.com/liangjingkanji/Net/").await()
    tvFragment.text = Put<String>("https://github.com/liangjingkanji/Net/").await()
    tvFragment.text = Patch<String>("https://github.com/liangjingkanji/Net/").await()
    tvFragment.text = Delete<String>("https://github.com/liangjingkanji/Net/").await()
    tvFragment.text = Trace<String>("https://github.com/liangjingkanji/Net/").await()
    tvFragment.text = Options<String>("https://github.com/liangjingkanji/Net/").await()
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