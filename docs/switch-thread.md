所有`scope*`前缀函数的创建的作用域都默认情况下为主线程, 即可以在作用域内直接操作UI.

既然是默认当然也可以直接修改scope内的作用域线程也可以在里面进行多次切换

## 切换作用域调度器

```kotlin
scopeNetLife(dispatcher = Dispatchers.IO) {
    binding.tvFragment.text = Get<String>("api").await()
}
```

## 在作用域内部切换

可能某些情况只是作用域内部分阻塞任务需要在其他线程执行. 我们可以直接在作用域内部进行多次的线程切换

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

|函数|描述|
|-|-|
|[withMain](api/-net/com.drake.net.utils/with-main.html)|切换到主线程|
|[withIO](api/-net/com.drake.net.utils/with-i-o.html)|切换到IO线程|
|[withDefault](api/-net/com.drake.net.utils/with-default.html)|切换到默认线程(属于子线程)|
|[withUnconfined](api/-net/com.drake.net.utils/with-unconfined.html)|切换到无限制调度器, 其取决于上一个执行的线程切换|
|launch|无返回值的协程挂起函数, 可指定线程|
|async|有返回值的协程挂起函数, 但得通过`await()`返回值. 可指定线程|
|[runMain](api/-net/com.drake.net.utils/with-unconfined.html)|切换到主线程, 该函数可以在任何地方调用不限于协程作用域|

- `with*`函数属于调用就立即执行, 在作用域内会阻塞(不会阻塞主线程)
- `launch/async` 属于执行并发任务, 两者区分就是有无返回值

> 在协程中切换线程也被成为切换调度器