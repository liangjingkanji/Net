<p >
    <a href="https://github.com/liangjingkanji/Net">中文</a>
    | <a href="https://github.com/liangjingkanji/Net/blob/master/README_EN.md">English</a>
</p>

<p align="center"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/logo.gif" width="300"/>

<p align="center"><strong>A Comprehensive Android Network Request Library Based on Coroutines</strong></p>

<p align="center"><a href="http://liangjingkanji.github.io/Net/">Documentation</a>
 | <a href="https://github.com/liangjingkanji/document/blob/master/visit-pages.md">Access Issues?</a>
 | <a href="https://liangjingkanji.github.io/document/">Contribute</a>
 | <a href="https://github.com/liangjingkanji/Net/releases/latest/download/net-sample.apk">Download Demo</a>
</p>

<p align="center"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/code-preview.png" width="400"/></p>

<p align="center">
<a href="https://jitpack.io/#liangjingkanji/Net"><img src="https://jitpack.io/v/liangjingkanji/Net.svg"/></a>
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-MIT-blue"/>
<a href="https://liangjingkanji.github.io/Net/api/"><img src="https://img.shields.io/badge/api-reference-red"/></a>
<a href="https://raw.githubusercontent.com/liangjingkanji/liangjingkanji/master/img/group-qrcode.png"><img src="https://raw.githubusercontent.com/liangjingkanji/liangjingkanji/master/img/group.svg"/></a>
<a href="http://liangjingkanji.github.io/Net/updates"><img src="https://img.shields.io/badge/updates-changelog-brightgreen"/></a>
<a href="https://github.com/liangjingkanji/Net/blob/master/docs/issues.md"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/issues.svg"/></a>
</p>

<p align="center"><img src="https://raw.githubusercontent.com/liangjingkanji/Net/master/docs/img/preview.png" align="center" width="30%;" /></p>

<br>

Net is a non-intrusive framework based on [OkHttp](https://github.com/square/okhttp)/Coroutines (compatible with all OkHttp APIs), allowing OkHttp version upgrades to maintain network security.

<br>

[Net 1.x](https://github.com/liangjingkanji/Net/tree/1.x) version implemented with RxJava <br>
[Net 2.x](https://github.com/liangjingkanji/Net/tree/2.x) version implemented with Coroutines <br>
[Net-okhttp3](https://github.com/liangjingkanji/Net-okhttp3) Net3.x compatibility library for lower Android versions <br>
[Net 3.x](https://github.com/liangjingkanji/Net/) version implemented with Coroutines, customizable OkHttp version

<br>
<p align="center"><strong>Contributions and issues are welcome</strong></p>

## Features

- [x] #1 in Development Efficiency
- [x] Designed Specifically for Android
- [x] OkHttp Best Practices
- [x] High-performance Okio
- [x] Supports All OkHttp APIs
- [x] Upgradable OkHttp Version for Network Security
- [x] Detailed Documentation/Low Learning Curve
- [x] Continuous Community Maintenance

## Main Functions

- [x] Coroutine Concurrency (No Coroutine Knowledge Required)
- [x] Concurrent/Serial/Queue/Synchronous Requests
- [x] Quick Thread Switching
- [x] Global Error Handling (Reduced Crash Rate)
- [x] Coroutine Scope with Error and Completion Callbacks
- [x] Converter for Any Data (json/protocol...)
- [x] Generic Type Support for Network Request Returns
- [x] Automatic Pull-to-Refresh and Load-More Handling
- [x] Automatic Pagination Loading
- [x] Automatic Default Page Display
- [x] Automatic Loading Dialog Display
- [x] Automatic Request Cancellation (Lifecycle)
- [x] Automatic Toast Error Messages
- [x] Automatic Exception Catching
- [x] ViewModel Support
- [x] Request Data Carrying (setExtra/tagOf)
- [x] Request ID/Group Support
- [x] Log Output (AndroidStudio Plugin/App Notification)
- [x] Return Fastest Result from Concurrent Requests
- [x] Global Request Cancellation
- [x] Quick HTTPS Configuration
- [x] Cookie Persistence Management
- [x] Timed/Time-Limited Requests
- [x] Force Cache Mode/Custom Cache Key/Cache Validity/LRU Cache Algorithm/Cache Any Data
- [x] Cache-then-Network Request (Preview Mode)
- [x] Built-in Powerful Polling (Timer)
- [x] Monitor Upload/Download Progress (Time Used, Speed per Second, Time Remaining...)

<br>

## Installation

Add repository to Project's settings.gradle

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add framework dependency to Module's build.gradle

```groovy
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0" // Coroutines (version optional)
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0'
implementation 'com.squareup.okhttp3:okhttp:4.11.0' // Requires OkHttp4+
implementation 'com.github.liangjingkanji:Net:3.7.0'
```
For development on Android 5 (API level 21) and below, please use [Net-okhttp3](https://github.com/liangjingkanji/Net-okhttp3)
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