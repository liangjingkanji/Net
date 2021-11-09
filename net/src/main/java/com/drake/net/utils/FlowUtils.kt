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

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.drake.net.scope.AndroidScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce

/**
 * 收集Flow结果并过滤重复结果
 */
@OptIn(InternalCoroutinesApi::class)
inline fun <T> Flow<T>.listen(
    owner: LifecycleOwner? = null,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): AndroidScope = AndroidScope(owner, event, dispatcher).launch {
    this@listen.collect(object : FlowCollector<T> {
        override suspend fun emit(value: T) = action(this@launch, value)
    })
}

/**
 * Flow直接创建作用域
 * @param owner 跟随的生命周期组件
 * @param event 销毁时机
 * @param dispatcher 指定调度器
 */
@Deprecated("规范命名", ReplaceWith("launchIn"), DeprecationLevel.ERROR)
@OptIn(InternalCoroutinesApi::class)
inline fun <T> Flow<T>.scope(
    owner: LifecycleOwner? = null,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): AndroidScope = launchIn(owner, event, dispatcher, action)

/**
 * Flow直接创建作用域
 * @param owner 跟随的生命周期组件
 * @param event 销毁时机
 * @param dispatcher 指定调度器
 */
@OptIn(InternalCoroutinesApi::class)
inline fun <T> Flow<T>.launchIn(
    owner: LifecycleOwner? = null,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): AndroidScope = AndroidScope(owner, event, dispatcher).launch {
    this@launchIn.collect(object : FlowCollector<T> {
        override suspend fun emit(value: T) = action(this@launch, value)
    })
}

/**
 * 为EditText的输入框文本变化启用节流阀, 即超过指定时间后(默认800毫秒)的输入框文本变化事件[TextWatcher.onTextChanged]会被下游收集到
 *
 * @param timeoutMillis 节流阀超时时间/单位毫秒, 默认值为800
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
fun EditText.debounce(timeoutMillis: Long = 800) = callbackFlow<String> {

    val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {

        }

        override fun afterTextChanged(s: Editable) {
            offer(s.toString())
        }
    }

    addTextChangedListener(textWatcher)
    awaitClose { this@debounce.removeTextChangedListener(textWatcher) }
}.debounce(timeoutMillis)

