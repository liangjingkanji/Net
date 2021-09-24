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

package com.drake.net.request

import com.drake.net.NetConfig
import com.drake.net.body.peekString
import com.drake.net.convert.NetConverter
import com.drake.net.interfaces.ProgressListener
import com.drake.net.tag.NetLabel
import okhttp3.OkHttpUtils
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.Request
import java.net.URLDecoder
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KType

//<editor-fold desc="请求属性">

/**
 * 请求的Id
 */
var Request.id: Any?
    get() = label<NetLabel.RequestId>()
    set(value) {
        setLabel(NetLabel.RequestId(value))
    }

/**
 * 请求的分组名
 * Group和Id本质上都是任意对象Any. 但是Net网络请求中自动取消的操作都是通过Group分组. 如果你覆盖可能会导致自动取消无效
 * 在设计理念上分组可以重复. Id不行
 */
var Request.group: Any?
    get() = label<NetLabel.RequestGroup>()
    set(value) {
        setLabel(NetLabel.RequestGroup(value))
    }

/**
 * 是否输出网络请求日志
 * 该属性和[NetConfig.logEnabled]有所区别
 * @see [com.drake.net.interceptor.LogRecordInterceptor]
 */
var Request.logRecord: Boolean?
    get() = label<NetLabel.LogRecord>()?.enabled
    set(value) {
        setLabel(value?.let { NetLabel.LogRecord(it) })
    }

/**
 * KType属于Kotlin特有的Type, 某些kotlin独占框架可能会使用到. 例如 kotlin.serialization
 */
var Request.kType: KType?
    get() = label<NetLabel.RequestKType>()?.value
    set(value) {
        setLabel(NetLabel.RequestKType(value))
    }

@Deprecated("建议使用属性", ReplaceWith("id"))
fun Request.id(): Any? {
    return id
}

@Deprecated("建议使用属性", ReplaceWith("group"))
fun Request.group(): Any? {
    return group
}

@Deprecated("建议使用属性", ReplaceWith("logRecord"))
fun Request.isLogRecord(): Boolean? {
    return logRecord
}

@Deprecated("建议使用属性", ReplaceWith("kType"))
fun Request.kType(): KType? {
    return kType
}

//</editor-fold>

//<editor-fold desc="Request.Builder">

/**
 * 设置请求Id
 */
fun Request.Builder.setId(id: Any?) = apply {
    setLabel(NetLabel.RequestId(id))
}

/**
 * 设置请求分组
 */
fun Request.Builder.setGroup(group: Any?) = apply {
    setLabel(NetLabel.RequestGroup(group))
}

/**
 * 设置是否记录日志
 */
fun Request.Builder.setLogRecord(enabled: Boolean) = apply {
    setLabel(NetLabel.LogRecord(enabled))
}

/**
 * 设置KType
 */
fun Request.Builder.setKType(type: KType) = apply {
    setLabel(NetLabel.RequestKType(type))
}

/**
 * 全部的请求头
 */
fun Request.Builder.headers(): Headers.Builder {
    return OkHttpUtils.headers(this)
}
//</editor-fold>

//<editor-fold desc="标签">

/**
 * 返回键值对的标签
 * 键值对标签即OkHttp中的实际tag(在Net中叫label)中的一个Map集合
 */
fun Request.tag(name: String): Any? {
    return label<NetLabel.Tags>()?.get(name)
}

/**
 * 设置键值对的标签
 */
fun Request.setTag(name: String, value: Any?) {
    val tags = tags()
    if (value == null) {
        tags.remove(name)
    } else {
        tags[name] = value
    }
}

/**
 * 设置键值对的tag
 */
fun Request.Builder.setTag(name: String, value: Any?) = apply {
    val tags = tags()
    if (value == null) {
        tags.remove(name)
    } else {
        tags[name] = value
    }
}

/**
 * 全部键值对标签
 */
fun Request.tags(): HashMap<String, Any?> {
    var tags = label<NetLabel.Tags>()
    if (tags == null) {
        tags = NetLabel.Tags()
        setLabel(tags)
    }
    return tags
}

/**
 * 全部键值对标签
 */
fun Request.Builder.tags(): HashMap<String, Any?> {
    var tags = label<NetLabel.Tags>()
    if (tags == null) {
        tags = NetLabel.Tags()
        setLabel(tags)
    }
    return tags
}

/**
 * 返回OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.label(): T? {
    return tag(T::class.java)
}

/**
 * 返回OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.Builder.label(): T? {
    return labels()[T::class.java] as? T
}

/**
 * 设置OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.setLabel(value: T) = apply {
    val labels = labels()
    if (value == null) {
        labels.remove(T::class.java)
    } else {
        labels[T::class.java] = value
    }
}

/**
 * 设置OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.Builder.setLabel(value: T) = apply {
    tag(T::class.java, value)
}

/**
 * 标签集合
 */
fun Request.labels(): MutableMap<Class<*>, Any?> {
    return OkHttpUtils.tags(this)
}

/**
 * 标签集合
 */
fun Request.Builder.labels(): MutableMap<Class<*>, Any?> {
    return OkHttpUtils.tags(this)
}

//</editor-fold>

//<editor-fold desc="下载">
/**
 * 当指定下载目录存在同名文件是覆盖还是进行重命名, 重命名规则是: $文件名_($序号).$后缀
 */
fun Request.downloadConflictRename(): Boolean {
    return label<NetLabel.DownloadFileConflictRename>()?.enabled == true
}

/**
 * 是否进行校验文件md5, 如果校验则匹配上既马上返回文件而不会进行下载
 */
fun Request.downloadMd5Verify(): Boolean {
    return label<NetLabel.DownloadFileMD5Verify>()?.enabled == true
}

/**
 * 下载文件目录
 */
fun Request.downloadFileDir(): String {
    return label<NetLabel.DownloadFileDir>()?.dir ?: NetConfig.app.filesDir.absolutePath
}

/**
 * 下载文件名
 */
fun Request.downloadFileName(): String? {
    return label<NetLabel.DownloadFileName>()?.name
}

/**
 * 下载的文件名称是否解码
 * 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称
 */
fun Request.downloadFileNameDecode(): Boolean {
    return label<NetLabel.DownloadFileNameDecode>()?.enabled == true
}

/**
 * 下载是否使用临时文件
 * 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名
 * 临时文件命名规则: 文件名 + .net-download
 *      下载文件名: install.apk, 临时文件名: install.apk.net-download
 */
fun Request.downloadTempFile(): Boolean {
    return label<NetLabel.DownloadTempFile>()?.enabled == true
}
//</editor-fold>

//<editor-fold desc="进度监听">
/**
 * 全部的上传监听器
 */
fun Request.uploadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    var uploadListeners = label<NetLabel.UploadListeners>()
    if (uploadListeners == null) {
        uploadListeners = NetLabel.UploadListeners()
        setLabel(uploadListeners)
    }
    return uploadListeners
}

/**
 * 全部的上传监听器
 */
fun Request.Builder.uploadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    var uploadListeners = label<NetLabel.UploadListeners>()
    if (uploadListeners == null) {
        uploadListeners = NetLabel.UploadListeners()
        setLabel(uploadListeners)
    }
    return uploadListeners
}

/**
 * 全部的下载监听器
 */
fun Request.downloadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    var downloadListeners = label<NetLabel.DownloadListeners>()
    if (downloadListeners == null) {
        downloadListeners = NetLabel.DownloadListeners()
        setLabel(downloadListeners)
    }
    return downloadListeners
}

/**
 * 全部的下载监听器
 */
fun Request.Builder.downloadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    var downloadListeners = label<NetLabel.DownloadListeners>()
    if (downloadListeners == null) {
        downloadListeners = NetLabel.DownloadListeners()
        setLabel(downloadListeners)
    }
    return downloadListeners
}

/**
 * 添加上传监听器
 */
fun Request.addUploadListener(progressListener: ProgressListener) {
    uploadListeners().add(progressListener)
}

/**
 * 添加下载监听器
 */
fun Request.addDownloadListener(progressListener: ProgressListener) {
    downloadListeners().add(progressListener)
}
//</editor-fold>

/**
 * 返回请求包含的转换器
 */
fun Request.converter(): NetConverter {
    return label<NetConverter>() ?: NetConfig.converter
}

/**
 * 设置转换器
 */
fun Request.Builder.setConverter(converter: NetConverter) = apply {
    setLabel(converter)
}

/**
 * 请求日志信息
 * 只会输出 application/x-www-form-urlencoded, application/json, text/`*` 的请求体类型日志
 * @param urlDecode 是否进行 UTF-8 URLDecode
 */
fun Request.logString(byteCount: Long = 1024 * 1024, urlDecode: Boolean = true): String? {
    val mediaType = body?.contentType() ?: return null
    val bodyString =
        if (body is FormBody || mediaType.type == "text" || mediaType.subtype == "json") {
            body?.peekString(byteCount)
        } else "Not support this type $mediaType"
    return if (urlDecode) {
        try {
            URLDecoder.decode(bodyString, "UTF-8")
        } catch (e: Exception) {
            bodyString
        }
    } else bodyString
}