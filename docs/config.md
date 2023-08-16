全局配置应在`Application.onCreate`中配置

## 初始配置

两种方式初始配置, 不初始化也能直接使用

=== "Net初始化"
    ```kotlin
    NetConfig.initialize(Api.HOST, this) {
        // 超时配置, 默认是10秒, 设置太长时间会导致用户等待过久
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        setDebug(BuildConfig.DEBUG)
        setConverter(SerializationConverter())
    }
    ```

=== "OkHttp构造器初始化"
    ```kotlin
    val okHttpClientBuilder = OkHttpClient.Builder()
        .setDebug(BuildConfig.DEBUG)
        .setConverter(SerializationConverter())
        .addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
    NetConfig.initialize(Api.HOST, this, okHttpClientBuilder)
    ```

如果请求指定Path会和上面`Api.HOST`组成Url后发起请求

!!! failure "强制初始化"
    如果是多进程项目(例如Xposed)必须初始化, 因为多进程无法自动指定Context

| 可配置选项 | 描述 |
|-|-|
| setDebug | 开启日志 |
| setSSLCertificate | 配置Https证书 |
| trustSSLCertificate | 信任所有Https证书 |
| setConverter | [转换器](converter-customize.md), 将请求结果转为任何类型 |
| setRequestInterceptor | [请求拦截器](interceptor.md), 全局请求头/请求参数 |
| setErrorHandler | [全局错误处理](error-global.md) |
| setDialogFactory | [全局对话框](auto-dialog.md) |

!!! success "修改配置"
    NetConfig存储所有全局配置变量, 可以后续修改, 且大部分支持单例指定配置

## 重试次数

可以添加`RetryInterceptor`拦截器即可实现失败以后会重试指定次数

默认情况下设置超时时间即可, OkHttp内部也有重试机制

```kotlin
NetConfig.initialize(Api.HOST, this) {
    // ... 其他配置
    addInterceptor(RetryInterceptor(3)) // 如果全部失败会重试三次
}
```
!!! warning "长时间阻碍用户交互"
     OkHttp内部也有重试机制, 如果还添加重试拦截器可能导致请求时间过长, 长时间阻碍用户交互


## 多域名

Net可随时变更请求域名, 以下介绍三种方式

=== "修改Host"
    ```kotlin
    NetConfig.host = Api.HOST_VIDEO
    ```

=== "指定全路径"
    建议使用单例类管理请求Url
    ```kotlin
    object Api {
        const val HOST_IMG = "http://127.0.0.1"
        const val BANNER = "$HOST_IMG/banner"
        const val HOME = "/home"
    }
    scopeNetLife {
        val banner = Get<BannerModel>(Api.BANNER).await()
        val home = Get<HomeModel>(Api.HOME).await()
    }
    ```

=== "使用拦截器"
    请求指定`tag`, 拦截器中根据tag修改Url, 建议tag为包含域名的枚举或单例
    ```kotlin
    scopeNetLife {
        val data = Get<String>(Api.PATH, GAME).await() // User即为tag
    }
    // 拦截器如何修改请搜索
    ```

## 网络安全配置

Net自动启用网络配置文件, 默认支持Http请求, 可自定义

```xml title="network_security_config.xml"
<network-security-config>
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```

!!! failure "无法打包Apk"
    当开发者自定义使用非同名`network_security_config`时网络配置文件时会无法打包Apk

    请添加`tools:replace`
    ```kotlin title="AndroidManifest.xml" hl_lines="3"
    <application
        tools:replace="android:networkSecurityConfig">
    ```
