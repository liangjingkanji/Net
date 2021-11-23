一般问题在GitHub的[问题](https://github.com/liangjingkanji/Net/issues)中搜索即可找到答案. 以下列举常见问题


### 无法打包
```
Attribute application@networkSecurityConfig value=(@xml/network_security_config_release) from AndroidManifest.xml:21:9-21
is also present at [com.github.liangjingkanji:Net:3.1.0] AndroidManifest.xml:18:9-69 value=(@xml/network_security_config).
Suggestion: add 'tools:replace="android:networkSecurityConfig"' to <application> element at AndroidManifest.xml:21:9-21:11 to override.
```
为了开箱即用Net默认使用了一个网络配置文件保证HTTP请求默认支持. 所以当你使用一个另一个名称(即不是network_security_config)的配置时会出现冲突 <br>
错误提示也给出解决方案: 在你项目中的AndroidManifest中添加一行`tools:replace="android:networkSecurityConfig"`

### 没有我需要的请求参数类型
默认支持的常见HTTP参数类型. 如果没有你需要的请求参数类型可以继承RequestBody实现

```kotlin
scopeNetLife {
    Post<String>("api"){
        body = MyRequestBody()
    }.await()
}
```

### 没有我需要的功能
Android上基本上99%都是基于OkHttp的网络请求解决方案. Net优秀之处在于完美支持OkHttp的所有功能组件.<br>
如果Net没有实现的功能可以百度/谷歌搜索`OkHttp如何实现**功能`