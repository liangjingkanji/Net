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

package com.drake.net.request

import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.interfaces.ProgressListener
import com.drake.net.tag.NetTag
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.OkHttpUtils
import okhttp3.Request
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KType

//<editor-fold desc="ID">
/**
 * 请求Id
 * Group和Id在使用场景上有所区别, 预期上Group允许重复赋值给多个请求, Id仅允许赋值给一个请求, 但实际上都允许重复赋值
 * 在作用域中发起请求时会默认使用协程错误处理器作为Group: `setGroup(coroutineContext[CoroutineExceptionHandler])`
 * 如果你覆盖Group会导致协程结束不会自动取消请求
 */
var Request.id: Any?
    get() = tagOf<NetTag.RequestId>()?.value
    set(value) {
        tagOf(value?.let { NetTag.RequestId(it) })
    }

/**
 * 请求分组
 * Group和Id在使用场景上有所区别, 预期上Group允许重复赋值给多个请求, Id仅允许赋值给一个请求, 但实际上都允许重复赋值
 * 在作用域中发起请求时会默认使用协程错误处理器作为Group: `setGroup(coroutineContext[CoroutineExceptionHandler])`
 * 如果你覆盖Group会导致协程结束不会自动取消请求
 */
var Request.group: Any?
    get() = tagOf<NetTag.RequestGroup>()?.value
    set(value) {
        tagOf(value?.let { NetTag.RequestGroup(it) })
    }
//</editor-fold>

/**
 * 为请求附着KType信息
 * KType属于Kotlin特有的Type, 某些Kotlin框架可能会使用到, 例如 kotlin.serialization
 */
var Request.kType: KType?
    get() = tagOf<NetTag.RequestKType>()?.value
    set(value) {
        tagOf(value?.let { NetTag.RequestKType(it) })
    }


//<editor-fold desc="Extra">
/**
 * 读取额外信息
 */
fun Request.extra(name: String): Any? {
    return tagOf<NetTag.Extras>()?.get(name)
}

/**
 * 全部额外信息
 */
fun Request.extras(): HashMap<String, Any?> {
    val tags = tags()
    return tags[NetTag.Extras::class.java] as NetTag.Extras? ?: kotlin.run {
        val tag = NetTag.Extras()
        tags[NetTag.Extras::class.java] = tag
        tag
    }
}
//</editor-fold>

//<editor-fold desc="Tag">
/**
 * 读取OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.tagOf(): T? {
    return tag(T::class.java)
}

/**
 * 设置OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.tagOf(value: T?) = apply {
    if (value == null) {
        tags().remove(T::class.java)
    } else {
        tags()[T::class.java] = value
    }
}

/**
 * 全部tag
 */
fun Request.tags(): MutableMap<Class<*>, Any?> {
    return OkHttpUtils.tags(this)
}

//</editor-fold>

//<editor-fold desc="Progress">
/**
 * 全部的上传监听器
 */
fun Request.uploadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    return tagOf<NetTag.UploadListeners>() ?: kotlin.run {
        val tag = NetTag.UploadListeners()
        tagOf(tag)
        tag
    }
}

/**
 * 全部的下载监听器
 */
fun Request.downloadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    return tagOf<NetTag.DownloadListeners>() ?: kotlin.run {
        val tag = NetTag.DownloadListeners()
        tagOf(tag)
        tag
    }
}

//</editor-fold>

//<editor-fold desc="Download">
/**
 * 下载文件路径存在同名文件时是覆盖或创建新文件(添加序号)
 * 重命名规则是: $文件名_($序号).$后缀, 例如`file_name(1).apk`
 */
fun Request.downloadConflictRename(): Boolean {
    return tagOf<NetTag.DownloadFileConflictRename>()?.value == true
}

/**
 * 下载文件MD5校验
 * 如果服务器响应头`Content-MD5`值和指定路径已经存在的文件MD5相同, 则跳过下载直接返回该File
 */
fun Request.downloadMd5Verify(): Boolean {
    return tagOf<NetTag.DownloadFileMD5Verify>()?.value == true
}

/**
 * 下载文件目录
 */
fun Request.downloadFileDir(): String {
    return tagOf<NetTag.DownloadFileDir>()?.value ?: NetConfig.app.filesDir.absolutePath
}

/**
 * 下载文件名
 */
fun Request.downloadFileName(): String? {
    return tagOf<NetTag.DownloadFileName>()?.value
}

/**
 * 下载的文件名称是否解码
 * 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称
 */
fun Request.downloadFileNameDecode(): Boolean {
    return tagOf<NetTag.DownloadFileNameDecode>()?.value == true
}

/**
 * 下载是否使用临时文件
 * 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名
 * 临时文件命名规则: 文件名 + .downloading
 *      下载文件名: install.apk, 临时文件名: install.apk.downloading
 */
fun Request.downloadTempFile(): Boolean {
    return tagOf<NetTag.DownloadTempFile>()?.value == true
}
//</editor-fold>

fun Request.setCacheComparison(cacheComparison : () -> File) {
    tagOf(NetTag.CacheComparison(cacheComparison))
}

fun Request.getCacheComparison():NetTag.CacheComparison? {
    return tagOf<NetTag.CacheComparison>()
}

/**
 * 返回请求包含的转换器
 */
fun Request.converter(): NetConverter {
    return tagOf<NetConverter>() ?: NetConfig.converter
}