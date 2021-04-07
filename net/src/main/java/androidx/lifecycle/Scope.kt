package androidx.lifecycle

import com.drake.net.scope.AndroidScope
import com.drake.net.scope.NetCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


private const val JOB_KEY = "com.drake.net.view-model"

/**
 * 在[ViewModel]被销毁时取消协程作用域
 */
fun ViewModel.scopeLife(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): AndroidScope {
    val scope: AndroidScope? = this.getTag(JOB_KEY)
    if (scope != null) return scope
    return setTagIfAbsent(JOB_KEY, AndroidScope(dispatcher = dispatcher).launch(block))
}

/**
 * 在[ViewModel]被销毁时取消协程作用域以及其中的网络请求
 * 具备网络错误全局处理功能, 其内部的网络请求会跟随其作用域的生命周期
 */
fun ViewModel.scopeNetLife(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): NetCoroutineScope {
    val scope: NetCoroutineScope? = this.getTag(JOB_KEY)
    if (scope != null) return scope
    return setTagIfAbsent(JOB_KEY, NetCoroutineScope(dispatcher = dispatcher).launch(block))
}
