多个请求并发执行

1. 当某个请求成功时, 剩余请求将被自动取消
2. 全部错误时则会抛出最后一个请求错误作为结果

!!! Note "异常信息"
    先失败的请求错误会被忽略, 但LogCat依然会输出异常

示例
```kotlin
scopeNetLife {

    // 同时发起四个网络请求
    val pathAsync = Get<String>("path") // 错误接口
    val pathAsync1 = Get<String>("path1") // 错误接口
    val pathAsync2 = Get<String>("path2")
    val pathAsync3 = Post<String>("path3")

    // 只返回最快的请求结果
    tv.text = fastest(pathAsync, pathAsync1, pathAsync2, pathAsync3)
}
```


## 取消剩余

上面的示例不会在获取到结果后取消剩余请求, 需设置同一请求分组才可以

```kotlin
scopeNetLife {
    // 同时发起四个网络请求
    val pathAsync = Get<String>("path") { setGroup("初始化") }
    val pathAsync1 = Post<String>("path1") { setGroup("初始化") }
    val pathAsync2 = Get<String>("path2") { setGroup("初始化") } // 错误接口
    val pathAsync3 = Get<String>("path3") { setGroup("初始化") } // 错误接口

    // 只返回最快的请求结果
    tv.text = fastest(listOf(pathAsync, pathAsync1, pathAsync2, pathAsync3), "初始化")
}
```

## 类型不一致

当需要返回结果, 但多接口返回数据类型不同, 使用`transform`转换为同一类型结果

```kotlin
scopeNetLife {

    val pathAsync = Post<String>("path").transform {
        Log.d("日志", "Post") // 如果该接口最快则会回调这里
        it // 这里可以返回其他数据结果
    }

    val pathAsync1 = Get<String>("path1").transform {
        Log.d("日志", "Get") // 如果该接口最快则会回调这里
        it
    }

    tv.text = fastest(pathAsync, pathAsync1)
}
```

只有最快返回结果的网络请求(或异步任务)的`transform`回调才会被执行到

## 捕获错误
```kotlin
scopeNetLife {
    val pathAsync = Get<String>("path")
    val pathAsync1 = Get<String>("path1")
    val pathAsync2 = Get<String>("path2")

    val data = try {
        fastest(listOf(pathAsync, pathAsync1, pathAsync2))
    // 当task/task1/task2全部错误后才并发执行backupTask/backupTask1
    } catch (e: Exception) {
        val pathAsync3 = Get<String>("path3")
        val pathAsync4 = Get<String>("path4")
        fastest(listOf(pathAsync3, pathAsync4))
    }
}
```