Net是基于[OkHttp](https://github.com/square/okhttp)/协程的非侵入式框架(可使用所有Api), 可升级OkHttp版本保持网络安全

<br>
<p align="center"><strong>STAR/分享可以让更多人参与到本开源项目</strong></p>
<br>


!!! note "前言"

    1. 阅读文档, 快速了解
    2. 阅读示例, 快速运用
    3. 阅读源码, 熟练并拓展

## 使用

<br>
发起网络请求的三个步骤

1. 创建作用域
1. 发起请求动作
1. 等待数据返回

=== "简单请求"
    ```kotlin
    scopeNetLife {
        // 大括号内属于作用域
        val data = Get<String>(Api.USER).await() // 发起GET请求并返回`String`
    }
    ```
=== "同步请求"
    ```kotlin
    scopeNetLife {
        // B将等待A请求返回结果后发起请求
        val userInfo = Get<UserInfo>(Api.USER).await() // A

        val config = Get<Config>(Api.CONFIG){ // B
            param("userId", userInfo.id) // 将上个请求结果作为参数
        }.await()
    }
    ```
=== "并发请求"
    ```kotlin
    scopeNetLife {
        // 两个请求同时发起
        val getUserInfoAsync = Get<UserInfo>(Api.USER)
        val getConfigAsync = Get<Config>(Api.CONFIG)

        val userInfo = getUserInfoAsync.await() // 等待数据返回
        val config = getConfigAsync.await()
    }
    ```

1. 多个网络请求在同一个作用域内可以统一管理
2. 如果多个网络请求之间毫无关联, 可以创建多个作用域来请求

!!! failure "强制初始化"
    多进程或Xposed项目要求先[初始化](config.md#_1)

自动识别`Url`或者`Path`

```kotlin
scopeNetLife {
    val userInfo = Get<String>("/net").await() // 等于"${NetConfig.host}/net"
    val config = Get<String>("https://github.com/liangjingkanji/net").await()
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
