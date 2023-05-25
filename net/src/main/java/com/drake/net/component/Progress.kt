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

    /** 全部字节数 */
    var totalByteCount: Long = 0
        internal set

    /** 距离上次进度变化的新增字节数 */
    var intervalByteCount: Long = 0
        internal set

    /** 距离上次进度变化的时间 */
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

    override fun toString(): String {
        return "Progress(currentByteCount=$currentByteCount, totalByteCount=$totalByteCount, finish=$finish)"
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