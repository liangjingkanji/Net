切换调度器可以理解为切换线程, 属于协程中的一个知识点

所有作用域默认情况下为主线程, 即可以在作用域内直接操作UI, 但是在创建异步任务的时候我们可能需要手动切换到其他调度器上

<br>
[SuspendUtils](https://github.com/liangjingkanji/Net/blob/master/net/src/main/java/com/drake/net/utils/Suspend.kt)
函数包含一些快捷切换调度器的`withContext`扩展函数

|函数|描述|
|-|-|
|[withMain](api/net/com.drake.net.utils/with-main.md)|切换到主线程调度器|
|[withIO](api/net/com.drake.net.utils/with-i-o.md)|切换到IO线程调度器|
|[withDefault](api/net/com.drake.net.utils/with-default.md)|切换到默认线程调度器|
|[withUnconfined](api/net/com.drake.net.utils/with-unconfined.md)|切换到无限制调度器|
|launch|无返回值的协程挂起函数|
|async|有返回值的协程挂起函数, 通过`await()`返回值|