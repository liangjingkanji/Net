/*
 * Copyright (C) 2018 Drake, https://github.com/liangjingkanji
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
 *
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING", "RedundantSetter")

package com.drake.net.component

import android.os.SystemClock
import android.text.format.DateUtils
import android.text.format.Formatter
import com.drake.net.NetConfig


/**
 * 下载/上传进度信息
 */
class Progress {

    /** 当前已经完成的字节数 */
    var currentByteCount: Long = 0
        internal set

    /** 当前已经完成的字节数 */
    var totalByteCount: Long = 0
        internal set

    /** 进度间隔时间内完成的字节数 */
    var intervalByteCount: Long = 0
        internal set

    /** 距离上次进度变化间隔时间 */
    var intervalTime: Long = 0
        internal set

    /** 是否完成 */
    var finish: Boolean = false
        internal set

    /** 开始下载的时间 */
    val startElapsedRealtime: Long = SystemClock.elapsedRealtime()

    /**
     * 每秒下载速度, 字节单位
     */
    var speedBytes = 0L
        get() {
            return if (intervalTime <= 0L || intervalByteCount <= 0) {
                field
            } else {
                field = intervalByteCount * 1000 / intervalTime
                field
            }
        }

    /**
     * 文件全部大小
     * 根据字节数自动显示内存单位, 例如 19MB 或者 27KB
     */
    fun totalSize(): String {
        val totalBytes = if (totalByteCount <= 0) 0 else totalByteCount
        return Formatter.formatFileSize(NetConfig.app, totalBytes)
    }

    /**
     * 已完成文件大小
     * 根据字节数自动显示内存单位, 例如 19MB 或者 27KB
     */
    fun currentSize(): String {
        return Formatter.formatFileSize(NetConfig.app, currentByteCount)
    }

    /**
     * 剩余大小
     * 根据字节数自动显示内存单位, 例如 19MB 或者 27KB
     */
    fun remainSize(): String {
        val remain = if (totalByteCount <= 0) 0 else totalByteCount - currentByteCount
        return Formatter.formatFileSize(NetConfig.app, remain)
    }


    /**
     * 每秒下载速度
     * 根据字节数自动显示内存单位, 例如 19MB 或者 27KB
     */
    fun speedSize(): String {
        return Formatter.formatFileSize(NetConfig.app, speedBytes)
    }

    /**
     * 请求或者响应的进度, 值范围在0-100
     * 如果服务器返回的响应体没有包含Content-Length(比如启用gzip压缩后Content-Length会被删除), 则无法计算进度, 始终返回0
     */
    fun progress(): Int {
        return when {
            finish || currentByteCount == totalByteCount -> 100
            totalByteCount <= 0 -> 0
            else -> (currentByteCount * 100 / totalByteCount).toInt()
        }
    }

    /**
     * 已使用时间
     */
    fun useTime(): String {
        return DateUtils.formatElapsedTime((SystemClock.elapsedRealtime() - startElapsedRealtime) / 1000)
    }

    /**
     * 已使用时间
     * @return 秒
     */
    fun useTimeSeconds(): Long {
        return (SystemClock.elapsedRealtime() - startElapsedRealtime) / 1000
    }

    /**
     * 剩余时间
     */
    fun remainTime(): String {
        val speedBytes = speedBytes
        val remainSeconds = if (totalByteCount <= 0 || speedBytes <= 0L) {
            0
        } else {
            (totalByteCount - currentByteCount) / speedBytes
        }
        return DateUtils.formatElapsedTime(remainSeconds)
    }

    /**
     * 剩余时间
     * @return 秒
     */
    fun remainTimeSeconds(): Long {
        val speedBytes = this.speedBytes
        return if (totalByteCount <= 0 || speedBytes <= 0L) {
            0
        } else {
            (totalByteCount - currentByteCount) / speedBytes
        }
    }
}