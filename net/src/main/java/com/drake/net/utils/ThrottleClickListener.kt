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

import android.view.View
import java.util.concurrent.TimeUnit

/**
 * 点击事件防抖动
 * @param interval 间隔时间, 如果为500表示500毫秒内只允许触发一次点击事件
 * @param unit 时间单位
 */
internal fun View.throttleClick(
    interval: Long = 500,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    block: View.() -> Unit
) {
    setOnClickListener(ThrottleClickListener(interval, unit, block))
}

internal class ThrottleClickListener(
    private val interval: Long = 500,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
    private var block: View.() -> Unit
) : View.OnClickListener {

    private var lastTime: Long = 0

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > unit.toMillis(interval)) {
            lastTime = currentTime
            block(v)
        }
    }
}