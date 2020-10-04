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
import com.drake.brv.PageRefreshLayout
import com.drake.net.NetConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
class PageCoroutineScope(
    val page: PageRefreshLayout,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : NetCoroutineScope(dispatcher = dispatcher) {

    val index get() = page.index

    init {
        page.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun start() {
        isReadCache = !page.loaded
        page.trigger()
    }

    override fun readCache(succeed: Boolean) {
        if (succeed && !animate) {
            page.showContent()
        }
        page.loaded = succeed
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        page.showError(e)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null || e is CancellationException) {
            page.showContent()
        }
        page.trigger()
    }

    override fun handleError(e: Throwable) {
        if (page.loaded || !page.stateEnabled) {
            NetConfig.onError(e)
        } else {
            NetConfig.onStateError(e, page)
        }
    }

}