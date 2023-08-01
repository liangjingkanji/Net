所有`scopeXX`作用域内为主线程, 可直接更新视图

!!! question "调度器"
    协程中的调度器实际上为线程池, 通过切换调度器可以切换到不同线程上

## 切换调度器

```kotlin
scopeNetLife(dispatcher = Dispatchers.IO) {
    binding.tvFragment.text = Get<String>("api").await()
}
```

## 作用域内部切换

有时需开启新的线程处理耗时任务

=== "主线程作用域内切换子线程"
    ```kotlin hl_lines="2"
    scopeNetLife {
        binding.tvFragment.text = withIO {
            // 假设此处是一个IO读写阻塞任务
            return "读出结果"
        }
    }
    ```

=== "子线程作用域内切换主线程"
    ```kotlin hl_lines="2"
    scopeNetLife(dispatcher = Dispatchers.IO) {
        binding.tvFragment.text = withMain {
            // 假设此处是一个IO读写阻塞任务
            return "读出结果"
        }
    }
    ```

| 函数                                                         | 描述                                                        |
| ------------------------------------------------------------ | ----------------------------------------------------------- |
| [withMain](api/-net/com.drake.net.utils/with-main.html)      | 切换到主线程                                                |
| [withIO](api/-net/com.drake.net.utils/with-i-o.html)         | 切换到IO线程                                                |
| [withDefault](api/-net/com.drake.net.utils/with-default.html) | 切换到子线程                                |
| [withUnconfined](api/-net/com.drake.net.utils/with-unconfined.html) | 切换到无限制调度器, 其取决于上一个执行的线程切换            |
| launch                                                       | 无返回值的挂起函数, 可指定线程                          |
| async                                                        | 有返回值的挂起函数, 通过`await()`返回值, 可指定线程 |
| [runMain](api/-net/com.drake.net.utils/with-unconfined.html) | 切换到主线程, 该函数不属于协程可以在任何地方调用      |

- `withXX()` 协程阻塞挂起
- `launch()/async()` 非阻塞执行, 两者区别是有无返回值
