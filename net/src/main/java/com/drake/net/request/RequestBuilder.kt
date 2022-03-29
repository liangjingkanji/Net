package com.drake.net.request

import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.interfaces.ProgressListener
import com.drake.net.tag.NetTag
import okhttp3.Headers
import okhttp3.OkHttpUtils
import okhttp3.Request
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KType

//<editor-fold desc="Group">
/**
 * 请求的Id
 */
var Request.Builder.id: Any?
    get() = tagOf<NetTag.RequestId>()
    set(value) {
        tagOf(NetTag.RequestId(value))
    }

/**
 * 请求的分组名
 * Group和Id本质上都是任意对象Any. 但是Net网络请求中自动取消的操作都是通过Group分组. 如果你覆盖可能会导致自动取消无效
 * 在设计理念上分组可以重复. Id不行
 */
var Request.Builder.group: Any?
    get() = tagOf<NetTag.RequestGroup>()
    set(value) {
        tagOf(NetTag.RequestGroup(value))
    }
//</editor-fold>

/**
 * 是否输出网络请求日志
 * 该属性和[NetConfig.logEnabled]有所区别
 * @see [com.drake.net.interceptor.LogRecordInterceptor]
 */
var Request.Builder.logRecord: Boolean
    get() = tagOf<NetTag.LogRecord>()?.enabled ?: false
    set(value) {
        tagOf(NetTag.LogRecord(value))
    }

/**
 * KType属于Kotlin特有的Type, 某些kotlin独占框架可能会使用到. 例如 kotlin.serialization
 */
var Request.Builder.kType: KType?
    get() = tagOf<NetTag.RequestKType>()?.value
    set(value) {
        tagOf(NetTag.RequestKType(value))
    }

/**
 * 全部的请求头
 */
fun Request.Builder.headers(): Headers.Builder {
    return OkHttpUtils.headers(this)
}
//</editor-fold>

//<editor-fold desc="Extra">
/**
 * 设置键值对的tag
 */
fun Request.Builder.setExtra(name: String, value: Any?) = apply {
    val extras = extras()
    if (value == null) {
        extras.remove(name)
    } else {
        extras[name] = value
    }
}

/**
 * 全部键值对标签
 */
fun Request.Builder.extras(): HashMap<String, Any?> {
    return tagOf<NetTag.Extras>() ?: kotlin.run {
        val tag = NetTag.Extras()
        tagOf(tag)
        tag
    }
}

//</editor-fold>

//<editor-fold desc="Tag">

/**
 * 返回OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.Builder.tagOf(): T? {
    return tags()[T::class.java] as? T
}

/**
 * 设置OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.Builder.tagOf(value: T) = apply {
    tag(T::class.java, value)
}

/**
 * 标签集合
 */
fun Request.Builder.tags(): MutableMap<Class<*>, Any?> {
    return OkHttpUtils.tags(this)
}
//</editor-fold>

//<editor-fold desc="Progress">
/**
 * 全部的上传监听器
 */
fun Request.Builder.uploadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    return tagOf<NetTag.UploadListeners>() ?: kotlin.run {
        val tag = NetTag.UploadListeners()
        tagOf(tag)
        tag
    }
}

/**
 * 全部的下载监听器
 */
fun Request.Builder.downloadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    return tagOf<NetTag.DownloadListeners>() ?: kotlin.run {
        val tag = NetTag.DownloadListeners()
        tagOf(tag)
        tag
    }
}
//</editor-fold>


/**
 * 设置转换器
 */
fun Request.Builder.setConverter(converter: NetConverter) = apply {
    tag(NetConverter::class.java, converter)
}