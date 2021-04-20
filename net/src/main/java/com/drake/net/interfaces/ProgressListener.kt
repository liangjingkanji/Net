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
package com.drake.net.interfaces

import com.drake.net.component.Progress

/**
 * 进度监听器, 为下载和上传两者
 *
 * @param interval 进度监听器刷新的间隔时间, 单位为毫秒, 默认值为500ms
 */
abstract class ProgressListener(
    var interval: Long = 500,
) {
    // 上次触发监听器时的开机时间
    var elapsedTime = 0L

    // 距离上次触发监听器的间隔字节数
    var intervalByteCount = 0L

    /**
     * 监听上传/下载进度回调函数
     */
    abstract fun onProgress(p: Progress)
}