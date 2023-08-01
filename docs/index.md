Net是基于[OkHttp](https://github.com/square/okhttp)/协程的非侵入式框架(可使用所有Api), 可升级OkHttp版本保持网络安全

<br>
<p align="center"><strong>STAR/分享可以让更多人参与到本开源项目</strong></p>
<br>


!!! note "前言"

    - 未实现功能可以搜索`"OkHttp如何XX"`来扩展
    - 阅读示例/源码来学习如何封装
    - 如果觉得文档看不懂那肯定是作者问题, 请反馈给作者或者自我修订

## 使用

<br>
发起网络请求的三个步骤

1. 创建作用域
1. 发起请求动作
1. 等待数据返回

=== "简单请求"
    ```kotlin
    scopeNetLife { 创建作用域
        // 这个大括号内就属于作用域内部
        val data = Get<String>("https://github.com/liangjingkanji/Net/").await() // 发起GET请求并返回`String`类型数据
    }
    ```
=== "同步请求"
    ```kotlin
    scopeNetLife {
        val userInfo = Get<String>("https://github.com/liangjingkanji/BRV/").await() // 立即请求
            val config = Get<String>("https://github.com/liangjingkanji/Net/"){
            param("userId", userInfo.id) // 使用上个请求的数据作为参数
        }.await() // 请求B 将等待A请求完毕后发起GET请求并返回数据
    }
    ```
=== "并发请求"
    ```kotlin
    scopeNetLife {
        // 以下两个网络请求属于同时进行中
        val getUserInfoAsync = Get<String>("https://github.com/liangjingkanji/Net/") // 立即请求
        val getConfigAsync = Get<String>("https://github.com/liangjingkanji/BRV/") // 立即请求

        val userInfo = getUserInfoAsync.await() // 等待数据返回
        val config = getConfigAsync.await()
    }
    ```

多个网络请求在同一个作用域内可以统一管理

如果多个网络请求之间毫无关联, 可以创建多个作用域来请求

!!! failure "强制初始化"
    多进程或Xposed项目要求先[初始化](config.md#_1)

并发请求错误示例

```kotlin hl_lines="3"
scopeNetLife {
    // 请求A
    val userInfo = Get<String>("https://github.com/liangjingkanji/Net/").await()
    // 由于上面使用`await()`函数, 所以必须等待A请求返回结果后才会执行B
    val getConfigAsync = Post<String>("https://github.com/liangjingkanji/Net/")

    val config = getConfigAsync.await() // 等待任务B返回数据
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

```kotlin
scopeNetLife {
    val file = Get<File>(Api.FILE).await()
}
```

详细查看[转换器](converter.md), 非以上类型要求[自定义转换器](converter-customize.md)

---
[下载Apk](https://github.com/liangjingkanji/Net/releases/latest/download/net-sample.apk){ .md-button }
[下载源码](https://github.com/liangjingkanji/Net.git){ .md-button }
[示例代码](https://github1s.com/liangjingkanji/Net/blob/HEAD/sample/src/main/java/com/drake/net/sample/ui/fragment/RequestMethodFragment.kt){ .md-button }
[界面演示](https://github.com/liangjingkanji/BRV){ .md-button }
