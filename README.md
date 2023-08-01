<p align="center"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/logo.gif" width="300"/>

<p align="center"><strong>不仅仅是网络请求的异步任务库</strong></p>

<p align="center"><a href="http://liangjingkanji.github.io/Net/">使用文档</a>
 | <a href="https://github.com/liangjingkanji/document/blob/master/visit-pages.md">无法访问?</a>
 | <a href="https://liangjingkanji.github.io/document/">贡献代码</a>
 | <a href="https://github.com/liangjingkanji/Net/releases/latest/download/net-sample.apk">下载体验</a>
</p>

<p align="center"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/code-preview.png" width="400"/></p>

<p align="center">
<a href="https://jitpack.io/#liangjingkanji/Net"><img src="https://jitpack.io/v/liangjingkanji/Net.svg"/></a>
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-MIT-blue"/>
<a href="https://liangjingkanji.github.io/Net/api/"><img src="https://img.shields.io/badge/api-%E5%87%BD%E6%95%B0%E6%96%87%E6%A1%A3-red"/></a>
<a href="https://raw.githubusercontent.com/liangjingkanji/liangjingkanji/master/img/group-qrcode.png"><img src="https://raw.githubusercontent.com/liangjingkanji/liangjingkanji/master/img/group.svg"/></a>
<a href="http://liangjingkanji.github.io/Net/updates"><img src="https://img.shields.io/badge/updates-%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97-brightgreen"/></a>
<a href="https://github.com/liangjingkanji/Net/blob/master/docs/issues.md"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/issues.svg"/></a>
</p>

<p align="center"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/preview.png" align="center" width="30%;" /></p>

<br>

Net是基于[OkHttp](https://github.com/square/okhttp)/协程的非侵入式框架(可使用所有Api), 可升级OkHttp版本保持网络安全

<br>

Welcome to international translation of this project's documents/notes, thank you for your support!

[Net 1.x](https://github.com/liangjingkanji/Net/tree/1.x) 版本使用RxJava实现 <br>
[Net 2.x](https://github.com/liangjingkanji/Net/tree/2.x) 版本使用协程实现 <br>
[Net-okhttp3](https://github.com/liangjingkanji/Net-okhttp3) Net3.x的Android低版本兼容库 <br>
[Net 3.x](https://github.com/liangjingkanji/Net/) 版本使用协程实现, 可自定义OkHttp版本

<br>
<p align="center"><strong>欢迎贡献代码/问题</strong></p>

## 特点

- [x] 开发效率No.1
- [x] 专为Android而生
- [x] OkHttp最佳实践
- [x] 使用高性能Okio
- [x] 支持OkHttp所有Api
- [x] 随时升级OkHttp版本保证网络安全性
- [x] 详细文档/低学习成本
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

Project 的 settings.gradle 添加仓库

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Module 的 build.gradle 添加依赖框架

```groovy
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0" // 协程(版本自定)
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0'
implementation 'com.squareup.okhttp3:okhttp:4.11.0' // 要求OkHttp4以上
implementation 'com.github.liangjingkanji:Net:3.6.0'
```
如果在 Android 5 (API level 21)以下开发, 请使用 [Net-okhttp3](https://github.com/liangjingkanji/Net-okhttp3)
<br>

## Contribute

<img src="https://s2.loli.net/2022/04/24/qrYuJ5lg67kb31n.jpg" width="100"/>

supported by [JetBrains](https://www.jetbrains.com/)


## License

```
MIT License

Copyright (c) 2023 劉強東

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
