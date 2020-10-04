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

import android.database.Cursor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.drake.net.scope.AndroidScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * 方便迭代Cursor, 针对数据库的便携函数
 */
fun Cursor.foreach(block: Cursor.() -> Unit) {
    if (count == 0) {
        close()
        return
    }
    moveToFirst()
    do {
        block()
    } while (moveToNext())
    close()
}

/**
 * 快速创建LiveData的观察者
 */
fun <M> LifecycleOwner.observe(liveData: LiveData<M>?, block: M?.() -> Unit) {
    liveData?.observe(this, Observer { it.block() })
}

/**
 * 收集Flow结果并过滤重复结果
 */
fun <T> Flow<List<T>>.listen(
    lifecycleOwner: LifecycleOwner? = null,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: (List<T>) -> Unit
): AndroidScope {
    return AndroidScope(lifecycleOwner, lifeEvent, dispatcher).launch {
        distinctUntilChanged().collect { data ->
            block(data)
        }
    }
}

/**
 * 继承这个类可以快速创建具备saveInstance的ViewModel
 */
open class SavedViewModel(var saved: SavedStateHandle) : ViewModel()

/**
 * 返回当前组件指定的ViewModel
 */
inline fun <reified M : ViewModel> ViewModelStoreOwner.getViewModel(): M {
    return ViewModelProvider(this).get(M::class.java)
}

/**
 * 返回当前组件指定的SavedViewModel
 */
inline fun <reified M : ViewModel> FragmentActivity.getSavedModel(): M {
    return ViewModelProvider(this, SavedStateViewModelFactory(application, this)).get(M::class.java)
}

/**
 * 返回当前组件指定的SavedViewModel
 */
inline fun <reified M : ViewModel> Fragment.getSavedModel(): M {
    return ViewModelProvider(this, SavedStateViewModelFactory(activity!!.application, this)).get(M::class.java)
}
