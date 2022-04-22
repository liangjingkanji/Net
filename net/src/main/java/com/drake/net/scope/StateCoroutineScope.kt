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

package com.drake.net.scope

import android.view.View
import com.drake.net.NetConfig
import com.drake.statelayout.StateLayout
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * 缺省页作用域
 */
class StateCoroutineScope(
    val state: StateLayout,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : NetCoroutineScope(dispatcher = dispatcher) {

    init {
        state.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun start() {
        isPreview = !state.loaded
        state.trigger()
    }

    override fun previewFinish(succeed: Boolean) {
        if (succeed) {
            state.showContent()
        }
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        if (!previewSucceed) state.showError(e)
    }

    override fun handleError(e: Throwable) {
        NetConfig.errorHandler.onStateError(e, state)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null || e is CancellationException) {
            state.showContent()
        }
        state.trigger()
    }

}
