/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.utils

import android.app.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.drake.brv.PageRefreshLayout
import com.drake.net.scope.*
import com.drake.statelayout.StateLayout
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

/**
 * 作用域内部全在主线程
 * 作用域全部属于异步
 * 作用域内部异常全部被捕获, 不会引起应用崩溃
 */

// <editor-fold desc="加载对话框">

/**
 * 作用域开始时自动显示加载对话框, 结束时自动关闭加载对话框
 * 可以设置全局对话框 [com.drake.net.NetConfig.onDialog]
 * @param dialog 仅该作用域使用的对话框
 *
 * 对话框被取消或者界面关闭作用域被取消
 */
fun FragmentActivity.scopeDialog(
        dialog: Dialog? = null,
        cancelable: Boolean = true,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit) =
        DialogCoroutineScope(this, dialog, cancelable, dispatcher).launch(block)

fun Fragment.scopeDialog(
        dialog: Dialog? = null,
        cancelable: Boolean = true,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit) =
        DialogCoroutineScope(requireActivity(), dialog, cancelable, dispatcher).launch(block)

// </editor-fold>


//<editor-fold desc="缺省页/分页">
/**
 * 自动处理缺省页的异步作用域
 * 作用域开始执行时显示加载中缺省页
 * 作用域正常结束时显示成功缺省页
 * 作用域抛出异常时显示错误缺省页
 * 并且自动吐司错误信息, 可配置 [com.drake.net.NetConfig.onStateError]
 * 自动打印异常日志
 * @receiver 当前视图会被缺省页包裹
 *
 * 布局被销毁或者界面关闭作用域被取消
 */
fun StateLayout.scope(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit): NetCoroutineScope {
    val scope = StateCoroutineScope(this, dispatcher)
    scope.launch(block)
    return scope
}

/**
 * PageRefreshLayout的异步作用域
 *
 * 1. 下拉刷新自动结束
 * 2. 上拉加载自动结束
 * 3. 捕获异常
 * 4. 打印异常日志
 * 5. 吐司部分异常[com.drake.net.NetConfig.onStateError]
 * 6. 判断添加还是覆盖数据
 * 7. 自动显示缺省页
 *
 * 布局被销毁或者界面关闭作用域被取消
 */
fun PageRefreshLayout.scope(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit): PageCoroutineScope {
    val scope = PageCoroutineScope(this, dispatcher)
    scope.launch(block)
    return scope
}
//</editor-fold>

//<editor-fold desc="异步任务">
/**
 * 异步作用域
 *
 * 该作用域生命周期跟随整个应用, 注意内存泄漏
 */
fun scope(dispatcher: CoroutineDispatcher = Dispatchers.Main,
          block: suspend CoroutineScope.() -> Unit): AndroidScope {
    return AndroidScope(dispatcher = dispatcher).launch(block)
}

fun LifecycleOwner.scopeLife(lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
                             dispatcher: CoroutineDispatcher = Dispatchers.Main,
                             block: suspend CoroutineScope.() -> Unit) = AndroidScope(this, lifeEvent, dispatcher).launch(block)

fun Fragment.scopeLife(lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP,
                       dispatcher: CoroutineDispatcher = Dispatchers.Main,
                       block: suspend CoroutineScope.() -> Unit) = AndroidScope(this, lifeEvent, dispatcher).launch(block)
//</editor-fold>

//<editor-fold desc="网络">
/**
 * 网络请求的异步作用域
 * 自动显示错误信息吐司
 *
 * 该作用域生命周期跟随整个应用, 注意内存泄漏
 */
fun scopeNet(dispatcher: CoroutineDispatcher = Dispatchers.Main,
             block: suspend CoroutineScope.() -> Unit) = NetCoroutineScope(dispatcher = dispatcher).launch(block)


fun LifecycleOwner.scopeNetLife(lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
                                dispatcher: CoroutineDispatcher = Dispatchers.Main,
                                block: suspend CoroutineScope.() -> Unit) = NetCoroutineScope(this, lifeEvent, dispatcher).launch(block)

/**
 * Fragment应当在[Lifecycle.Event.ON_STOP]时就取消作用域, 避免[Fragment.onDestroyView]导致引用空视图
 */
fun Fragment.scopeNetLife(lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP,
                          dispatcher: CoroutineDispatcher = Dispatchers.Main,
                          block: suspend CoroutineScope.() -> Unit) = NetCoroutineScope(this, lifeEvent, dispatcher).launch(block)
//</editor-fold>


/**
 * Flow直接创建作用域
 * @param owner 跟随的生命周期组件
 * @param event 销毁时机
 * @param dispatcher 指定调度器
 */
@OptIn(InternalCoroutinesApi::class)
inline fun <T> Flow<T>.scope(owner: LifecycleOwner? = null,
                             event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
                             dispatcher: CoroutineDispatcher = Dispatchers.Main,
                             crossinline action: suspend (value: T) -> Unit): AndroidScope = AndroidScope(owner, event, dispatcher).launch {
    this@scope.collect(object : FlowCollector<T> {
        override suspend fun emit(value: T) = action(value)
    })
}


