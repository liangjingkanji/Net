Net可以通过实现`NetErrorHandler`接口来监听全局错误处理, 当你通过`setErrorHandler`后Net就不会再执行默认的错误处理了

```kotlin
NetConfig.initialize("https://github.com/liangjingkanji/Net/") {

    setErrorHandler(object : NetErrorHandler() {
        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onStateError(e: Throwable, view: View) {
            super.onStateError(e, view)
        }
    })
}
```

|场景|处理函数|处理方式|
|-|-|-|
|普通网络请求/自动加载框|`onError`| 默认吐司错误信息 |
|使用自动处理缺省页的作用域|`onStateError`| 仅部分错误信息会吐司, 因为缺省页不需要所有的错误信息都吐司(toast)提示, 因为错误页可能已经展示错误信息, 所以这里两者处理的函数区分. |

> `scope/scopeLife`不会触发任何全局错误NetErrorHandler, 请使用单例错误处理方式`catch`, 因为其用于处理异步任务,不应当用于网络请求