<p align="center"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/logo.gif" width="300"/>

<p align="center"><strong>不仅仅是网络请求的异步任务库</strong></p>

<p align="center"><a href="http://liangjingkanji.github.io/Net/">使用文档</a>
 | <a href="https://github.com/liangjingkanji/document/blob/master/visit-pages.md">无法访问?</a>
 | <a href="https://github.com/liangjingkanji/Net/releases/download/3.5.3/net-sample.apk">下载体验</a>
</p>

<p align="center"><img src="https://user-images.githubusercontent.com/21078112/169665591-1bf3de50-888e-467a-9b64-0a9d03f73751.png" width="320"/></p>

<p align="center">
<a href="https://jitpack.io/#liangjingkanji/Net"><img src="https://jitpack.io/v/liangjingkanji/Net.svg"/></a>
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-Apache-blue"/>
<a href="https://liangjingkanji.github.io/Net/api/"><img src="https://img.shields.io/badge/api-%E5%87%BD%E6%95%B0%E6%96%87%E6%A1%A3-red"/></a>
<img src="https://raw.githubusercontent.com/liangjingkanji/liangjingkanji/master/img/group.svg"/>
<a href="http://liangjingkanji.github.io/Net/updates"><img src="https://img.shields.io/badge/updates-%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97-brightgreen"/></a>
<a href="https://github.com/liangjingkanji/Net/blob/master/docs/issues.md"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/issues.svg"/></a>
</p>

<p align="center"><img src="https://i.imgur.com/aBe7Ib2.png" align="center" width="30%;" /></p>

<br>

Android上可能是最强的网络框架, 基于[OkHttp](https://github.com/square/okhttp)/协程的非侵入式框架(不影响原有功能). 学习成本低/使用简单, 一行代码发起网络请求, 甚至无需初始化

<br>

欢迎将本项目文档/注释进行国际化翻译, 感谢您的支持! <br>
Welcome to international translation of this project's documents/notes, thank you for your support!

[Net 1.x](https://github.com/liangjingkanji/Net/tree/1.x) 版本使用RxJava实现 <br>
[Net 2.x](https://github.com/liangjingkanji/Net/tree/2.x) 版本使用协程实现 <br>
[Net 3.x](https://github.com/liangjingkanji/Net/) 版本使用协程实现, 可自定义OkHttp版本

<br>
<p align="center"><strong>欢迎贡献代码/问题</strong></p>

## 特点

- [x] 开发效率No.1
- [x] 专为Android而生
- [x] OkHttp最佳实践
- [x] 使用高性能Okio
- [x] 支持OkHttp所有功能/组件
- [x] 随时升级OkHttp版本保证网络安全性
- [x] 优秀的源码/注释/文档/示例
- [x] 永远保持社区维护

## 主要功能

- [x] 协程并发(不会协程也可上手)
- [x] 并发/串行/队列/同步请求
- [x] 快速切换线程
- [x] 全局错误处理(减少崩溃率)
- [x] 协程作用域支持错误和结束回调
- [x] 解析任何数据(json/protocol...)的转换器
- [x] 泛型指定网络请求返回任何类型
- [x] 自动处理下拉刷新和上拉加载
- [x] 自动分页加载
- [x] 自动显示缺省页
- [x] 自动显示加载对话框
- [x] 自动取消请求(生命周期)
- [x] 自动吐司错误信息
- [x] 自动捕获异常
- [x] 支持ViewModel
- [x] Request携带数据(setExtra/tagOf)
- [x] Request支持Id/Group分组
- [x] 日志信息输出(AndroidStudio插件/App通知栏)
- [x] 并发请求返回最快请求结果
- [x] 全局取消请求
- [x] Https快速配置
- [x] Cookie持久化管理
- [x] 定时/限时请求
- [x] 强制缓存模式/自定义缓存Key/缓存有效期/LRU缓存算法/缓存任何数据
- [x] 先缓存后网络请求(预览模式)
- [x] 内置超强轮询器(计时器)
- [x] 监听上传/下载进度(使用时间, 每秒速度, 剩余时间...)

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
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0" // 协程(版本自定)
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0'
implementation 'com.squareup.okhttp3:okhttp:4.10.0' // 要求OkHttp4以上
implementation 'com.github.liangjingkanji:Net:3.5.3'

// 支持自动下拉刷新和缺省页的(可选)
implementation 'com.github.liangjingkanji:BRV:1.3.86'
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
