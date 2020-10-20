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
package com.yanzhenjie.kalle

import java.util.concurrent.ConcurrentHashMap

object NetCancel {

    private val cancellerMap = ConcurrentHashMap<Canceller, Any>()

    /**
     * 添加一个网络请求取消者用于取消网络
     *
     * @param uid   target request.
     * @param canceller canceller.
     */
    @Synchronized
    fun add(uid: Any?, canceller: Canceller) {
        uid ?: return
        cancellerMap[canceller] = uid
    }

    /**
     * 删除网络请求ID
     *
     * @param uid target request.
     */
    @Synchronized
    fun remove(uid: Any?) {
        uid ?: return
        val iterator = cancellerMap.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value == uid) iterator.remove()
        }
    }

    /**
     * 取消网络请求
     *
     * @param uid 网络请求的ID
     */
    @Synchronized
    fun cancel(uid: Any?) {
        uid ?: return
        val iterator = cancellerMap.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (uid == next.value) {
                iterator.remove()
                next.key.cancel()
            }
        }
    }
}