/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：2/15/20 4:30 AM
 */

package com.drake.net.utils

import android.database.Cursor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.drake.net.scope.AndroidScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * 迭代Cursor
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
 * 观察LiveData
 */
fun <M> LifecycleOwner.observe(liveData: LiveData<M>?, block: M?.() -> Unit) {
    liveData?.observe(this, Observer { it.block() })
}

/**
 * 监听数据库
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T> Flow<List<T>>.listen(lifecycleOwner: LifecycleOwner? = null,
                             lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
                             block: (List<T>) -> Unit) {
    AndroidScope(lifecycleOwner, lifeEvent).launch {
        distinctUntilChanged().collect { data ->
            withMain {
                block(data)
            }
        }
    }
}

open class SavedViewModel(var saved: SavedStateHandle) : ViewModel()

inline fun <reified M : ViewModel> ViewModelStoreOwner.getViewModel(): M {
    return ViewModelProvider(this).get(M::class.java)
}

inline fun <reified M : ViewModel> FragmentActivity.getSavedModel(): M {
    return ViewModelProvider(
            this,
            SavedStateViewModelFactory(application, this)
                            ).get(M::class.java)
}


inline fun <reified M : ViewModel> Fragment.getSavedModel(): M {
    return ViewModelProvider(this, SavedStateViewModelFactory(activity!!.application, this)).get(M::class.java)
}
