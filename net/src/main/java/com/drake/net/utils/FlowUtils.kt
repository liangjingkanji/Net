/*
 * MIT License
 *
 * Copyright (c) 2023 劉強東 https://github.com/liangjingkanji
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
fun EditText.debounce(timeoutMillis: Long = 800) = callbackFlow {

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
            trySend(s.toString())
        }
    }
    addTextChangedListener(textWatcher)
    awaitClose { this@debounce.removeTextChangedListener(textWatcher) }
}.debounce(timeoutMillis)

