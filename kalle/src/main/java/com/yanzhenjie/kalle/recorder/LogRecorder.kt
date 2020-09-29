package com.yanzhenjie.kalle.recorder

import android.annotation.SuppressLint
import android.os.*
import android.util.Log
import com.yanzhenjie.kalle.recorder.LogRecorder.enabled
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicLong

/**
 * 日志记录器
 *
 * @property enabled 是否启用日志记录器
 */
object LogRecorder {

    var enabled = false

    private val handler: Handler
        get() {
            val handlerThread = HandlerThread("OkHttpProfiler", Process.THREAD_PRIORITY_BACKGROUND)
            handlerThread.start()
            return LogBodyHandler(handlerThread.looper)
        }

    private const val LOG_LENGTH = 4000
    private const val SLOW_DOWN_PARTS_AFTER = 20
    private const val LOG_PREFIX = "OKPRFL"
    private const val DELIMITER = "_"
    private const val HEADER_DELIMITER = ':'
    private const val SPACE = ' '
    private const val KEY_TAG = "TAG"
    private const val KEY_VALUE = "VALUE"
    private const val KEY_PARTS_COUNT = "PARTS_COUNT"
    private val format: DateFormat = SimpleDateFormat("ddhhmmssSSS", Locale.US)
    private val previousTime = AtomicLong()

    /**
     * 产生一个唯一的基于时间戳Id
     */
    @Synchronized
    fun generateId(): String {
        if (!enabled) return ""
        var currentTime: Long = format.format(Date()).toLong()
        var previousTime: Long = this.previousTime.get()
        if (currentTime <= previousTime) {
            currentTime = ++previousTime
        }
        this.previousTime.set(currentTime)
        return currentTime.toString(Character.MAX_RADIX)
    }

    /**
     * 发送请求信息到记录器中
     *
     * @param id 请求的唯一标识符
     * @param url 请求URL地址
     * @param method 请求方法
     * @param headers 请求头
     * @param body 请求体
     */
    fun recordRequest(
        id: String,
        url: String,
        method: String,
        headers: Map<String, List<String>>,
        body: String?
    ) {
        if (!enabled) return

        fastLog(id, MessageType.REQUEST_METHOD, method)
        fastLog(id, MessageType.REQUEST_URL, url)
        fastLog(id, MessageType.REQUEST_TIME, System.currentTimeMillis().toString())

        for ((key, value) in headers) {
            var header = value.toString()
            if (header.length > 2) header = header.substring(1, header.length - 1)
            fastLog(id, MessageType.REQUEST_HEADER, key + HEADER_DELIMITER + SPACE + header)
        }
        if (body != null) {
            largeLog(id, MessageType.REQUEST_BODY, body)
        } else {
            largeLog(id, MessageType.REQUEST_BODY, url)
        }
    }

    /**
     * 发送响应信息到记录器中
     *
     * @param id 请求的唯一标识符
     * @param code 响应码
     * @param headers 响应头
     * @param body 响应体
     */
    fun recordResponse(
        id: String,
        code: String,
        headers: Map<String, List<String>>,
        body: String?
    ) {
        if (!enabled) return
        largeLog(id, MessageType.RESPONSE_BODY, body)
        logWithHandler(id, MessageType.RESPONSE_STATUS, code, 0)
        for ((key, value) in headers) {
            var header = value.toString()
            if (header.length > 2) header = header.substring(1, header.length - 1)
            logWithHandler(id, MessageType.RESPONSE_HEADER, key + HEADER_DELIMITER + header, 0)
        }
    }

    /**
     * 发送请求异常到记录器
     *
     * @param id 请求的唯一标识符
     */
    fun recordException(id: String, response: Exception) {
        if (!enabled) return
        logWithHandler(id, MessageType.RESPONSE_ERROR, response.localizedMessage, 0)
    }

    /**
     * 发送请求到响应时间间隔
     *
     * @param id 请求的唯一标识符
     * @param duration 间隔时间
     */
    fun recordDuration(id: String, duration: Long) {
        if (!enabled) return
        logWithHandler(id, MessageType.RESPONSE_TIME, duration.toString(), 0)
        logWithHandler(id, MessageType.RESPONSE_END, "-->", 0)
    }

    @SuppressLint("LogNotTimber")
    private fun fastLog(id: String, type: MessageType, message: String?) {
        val tag = LOG_PREFIX + DELIMITER + id + DELIMITER + type.type
        if (message != null) {
            Log.v(tag, message)
        }
    }

    private fun logWithHandler(id: String, type: MessageType, message: String?, partsCount: Int) {
        val handlerMessage = handler.obtainMessage()
        val tag = LOG_PREFIX + DELIMITER + id + DELIMITER + type.type
        val bundle = Bundle()
        bundle.putString(KEY_TAG, tag)
        bundle.putString(KEY_VALUE, message)
        bundle.putInt(KEY_PARTS_COUNT, partsCount)
        handlerMessage.data = bundle
        handler.sendMessage(handlerMessage)
    }

    private fun largeLog(id: String, type: MessageType, content: String?) {
        val contentLength = content?.length ?: 0
        if (contentLength > LOG_LENGTH) {
            val parts = contentLength / LOG_LENGTH
            for (i in 0..parts) {
                val start = i * LOG_LENGTH
                var end = start + LOG_LENGTH
                if (end > contentLength) {
                    end = contentLength
                }
                logWithHandler(id, type, content?.substring(start, end), parts)
            }
        } else {
            logWithHandler(id, type, content, 0)
        }
    }

    private class LogBodyHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            if (bundle != null) {
                val partsCount = bundle.getInt(KEY_PARTS_COUNT, 0)
                if (partsCount > SLOW_DOWN_PARTS_AFTER) {
                    try {
                        Thread.sleep(5L)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                val data = bundle.getString(KEY_VALUE)
                val key = bundle.getString(KEY_TAG)
                if (data != null && key != null) {
                    Log.v(key, data)
                }
            }
        }
    }
}