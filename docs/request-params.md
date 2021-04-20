在Net中都是使用的其框架内部创建的`Request`创建请求

涉及到请求参数的类只有两个类和一个抽象父类

```kotlin
BaseRequest
    |- UrlRequest
    |- BodyRequest
```


根据请求方法不同使用的Request也不同

```kotlin
GET, HEAD, OPTIONS, TRACE, // Url request
POST, DELETE, PUT, PATCH // Body request
```

代码示例

```kotlin
scopeNetLife {
    Get<String>("api") {
        // this 即为 UrlRequest
    }.await

    Post<String>("api") {
        // this 即为 BodyRequest
    }.await
}
```

关于具体函数希望阅读源码. Net源码全部有文档注释, 以及函数结构分组

<img src="https://i.imgur.com/oZp9WYZ.png" width="420"/>

