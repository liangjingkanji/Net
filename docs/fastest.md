Net支持多个接口请求并发, 仅返回最快的请求结果, 剩余请求将被自动取消, 同样可以用于筛选掉无法响应的域名
<br>

> 接口请求错误被忽略(LogCat依然可以看到异常信息), 但如果所有请求全部异常则抛出最后一个请求的异常作为错误处理

示例
```kotlin
scopeNetLife {

    // 同时发起四个网络请求
    val deferred = Get<String>("api0") // 错误接口
    val deferred1 = Get<String>("api1") // 错误接口
    val deferred2 = Get<String>("api")
    val deferred3 = Post<String>("api")

    // 只返回最快的请求结果
    tvFragment.text = fastest(deferred, deferred1, deferred2, deferred3)
}
```


## 取消剩余

上面的示例代码实际上不会在获取到最快的结果后自动取消请求, 我们需要手动设置uid才可以

```kotlin
scopeNetLife {
    // 同时发起四个网络请求
    val deferred2 = Get<String>("api") { setGroup("最快") }
    val deferred3 = Post<String>("api") { setGroup("最快") }
    val deferred = Get<String>("api0") { setGroup("最快") } // 错误接口
    val deferred1 = Get<String>("api1") { setGroup("最快") } // 错误接口

    // 只返回最快的请求结果
    tvFragment.text = fastest(listOf(deferred, deferred1, deferred2, deferred3), "最快")
}
```

网络请求的取消本质上依靠uid来辨别,如果使用`setGroup`函数设置分组名称就可以在返回最快结果后取消掉其他网络请求, 反之不会取消其他网络请求
<br>

> uid可以是任何类型任何值, 只有请求的`setGroup`参数和`fastest`函数的group参数等于即可

<br>

## 类型不一致

假设并发的接口返回的数据类型不同  或者想要监听最快请求返回的结果回调请使用`transform`函数

```kotlin
scopeNetLife {

    val fastest = Post<String>("api").transform {
        Log.d("日志", "Post") // 如果该接口最快则会回调这里
        it // 这里可以返回其他数据结果
    }

    val fastest2 = Get<String>("api").transform {
        Log.d("日志", "Get") // 如果该接口最快则会回调这里
        it
    }

    tvFragment.text = fastest(fastest, fastest2)
}
```

有的场景下并发的接口返回的数据类型不同, 但是fastest只能返回一个类型, 我们可以使`transform`的回调函数返回结果都拥有一个共同的接口, 然后去类型判断

<br>

> 只有最快返回结果的网络请求(或异步任务)的`transform`回调才会被执行到

## 捕获Fastest
```kotlin
scopeNetLife {
    val task = Get<String>("api2")
    val task1 = Get<String>("api2")
    val task2 = Get<String>("api2")

    val data = try {
        fastest(task, task1, task2) // 当 task/task1/task2 全部异常之后再并发执行 backupTask/backupTask1
    } catch (e: Exception) {
        val backupTask = Get<String>("api2")
        val backupTask1 = Get<String>("api")
        fastest(backupTask, backupTask1)
    }
}
```


<br>

> 不要尝试使用这种方式来取代CDN加速