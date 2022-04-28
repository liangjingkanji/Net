<p align="center"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/logo.gif" width="300"/>

<p align="center"><strong>不仅仅是网络请求的异步任务库</strong></p>

<p align="center"><a href="http://liangjingkanji.github.io/Net/">使用文档</a>
 | <a href="https://coding-pages-bucket-3558162-8706000-16642-587704-1252757332.cos-website.ap-shanghai.myqcloud.com/">备用访问</a>
 | <a href="https://raw.githubusercontent.com/liangjingkanji/Net/master/sample/release/sample-release.apk">下载体验</a>
</p>

<p align="center"><img src="https://i.imgur.com/X06J6fK.jpg" width="400"/></p>

<p align="center">
<a href="https://jitpack.io/#liangjingkanji/Net"><img src="https://jitpack.io/v/liangjingkanji/Net.svg"/></a>
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-Apache-blue"/>
<a href="https://liangjingkanji.github.io/Net/api/"><img src="https://img.shields.io/badge/api-%E5%87%BD%E6%95%B0%E6%96%87%E6%A1%A3-red"/></a>
<a href="https://jq.qq.com/?_wv=1027&k=vWsXSNBJ"><img src="https://img.shields.io/badge/QQ群-752854893-blue"/></a>
<a href="http://liangjingkanji.github.io/Net/updates"><img src="https://img.shields.io/badge/updates-%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97-brightgreen"/></a>
<a href="https://github.com/liangjingkanji/Net/blob/master/docs/issues.md"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/issues.svg"/></a>
</p>

<p align="center"><img src="https://i.imgur.com/aBe7Ib2.png" align="center" width="30%;" /></p>

<br>

Android上可能是最强的网络框架, 基于OkHttp/协程的非侵入式框架(不影响原有功能). **一行代码**发起网络请求, 你甚至无需初始化 <br>
<br>

[Net 1.x](https://github.com/liangjingkanji/Net/tree/1.x) 版本使用RxJava实现 <br>
[Net 2.x](https://github.com/liangjingkanji/Net/tree/2.x) 版本使用协程实现 <br>
Net 3.x 版本使用OkHttp+协程实现, 可指定其OkHttp版本

<br>
<p align="center"><strong>欢迎贡献代码/问题</strong></p>
<br>

主要功能

- 开发速度No.1
- 高质量源码/注释/文档/示例
- 专为Android而生
- 支持OkHttp所有功能/组件
- DSL作用域编程
- 协程并发(不会协程也可上手)
- 并发/串行/队列/同步请求
- 快速切换线程
- 全局错误处理
- 协程作用域支持错误和结束回调
- JSON/Protocol/任何数据解析的转换器
- 网络请求返回指定泛型(String/Response/File/List/Map/Pair...)
- 自动处理下拉刷新和上拉加载
- 自动处理分页加载
- 自动缺省页
- 自动处理生命周期
- 自动处理加载对话框
- 自动错误信息吐司
- 自动异常捕获(定位请求代码位置)
- 支持ViewModel
- Request携带数据(setExtra/tagOf)
- Request支持Id/Group分组
- AS日志插件/应用通知栏日志
- 并发请求返回最快请求结果
- 全局取消请求/自动取消请求
- Https快速配置
- Cookie管理
- 强制缓存模式/自定义缓存Key/LRU缓存算法/缓存任何类型
- 缓存+网络双重读取(预览模式实现)
- 内置超强轮询器(计时器)
- 监听上传/下载进度(使用时间, 每秒速度, 剩余时间...)

<br>

## 安装

添加远程仓库根据创建项目的 Android Studio 版本有所不同

Android Studio Arctic Fox以下创建的项目 在项目根目录的 build.gradle 添加仓库

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Android Studio Arctic Fox以上创建的项目 在项目根目录的 settings.gradle 添加仓库

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

然后在 module 的 build.gradle 添加依赖框架

```groovy
// 协程库(版本可自定)
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1'
// OkHttp(版本可自定, 要求OkHttp4以上版本)
implementation 'com.squareup.okhttp3:okhttp:4.9.1'
// Net
implementation 'com.github.liangjingkanji:Net:3.4.5'

// 支持自动下拉刷新和缺省页的(可选)
implementation 'com.github.liangjingkanji:BRV:1.3.62'
```
如果你是在 Android 5 (API level 21)以下开发, 要求使用OkHttp3.x请使用: [Net-okhttp3](https://github.com/liangjingkanji/Net-okhttp3)
<br>

## Contribute

<img src="https://s2.loli.net/2022/04/24/qrYuJ5lg67kb31n.jpg" width="100"/>

supported by [JetBrains](https://www.jetbrains.com/)


## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
