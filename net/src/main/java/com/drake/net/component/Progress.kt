@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING", "RedundantSetter")

package com.drake.net.component

import android.os.SystemClock
import android.text.format.DateUtils
import android.text.format.Formatter
import com.drake.net.NetConfig

/**
 * @property currentByteCount 当前已经完成的字节数
 * @property totalByteCount 当前已经完成的字节数
 * @property intervalByteCount 进度间隔时间内完成的字节数
 * @property intervalTime 距离上次进度变化间隔时间
 * @property startElapsedRealtime 开始下载的时间
 * @property finish 是否完成
 */
data class Progress(
    var currentByteCount: Long = 0,
    var totalByteCount: Long = 0,
    var intervalByteCount: Long = 0,
    var intervalTime: Long = 0,
    val startElapsedRealtime: Long = SystemClock.elapsedRealtime(),
    var finish: Boolean = false,
) {

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
     * 请求或者响应的进度, 值范围在0-100
     * 如果服务器返回的响应体没有包含Content-Length(比如启用gzip压缩后Content-Length会被删除), 则无法计算进度, 始终返回0
     */
    fun progress(): Int {
        return when {
            currentByteCount == totalByteCount -> 100
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