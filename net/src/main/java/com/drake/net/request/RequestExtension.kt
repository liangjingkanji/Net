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
import okhttp3.FormBody
import okhttp3.Request
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KType

//<editor-fold desc="请求属性">
/**
 * 请求的Id
 */
fun Request.id(): Any? {
    return label<NetLabel.RequestId>()?.value
}

fun Request.Builder.setId(id: Any?) = apply {
    setLabel(NetLabel.RequestId(id))
}

/**
 * 请求的分组名
 */
fun Request.group(): Any? {
    return label<NetLabel.RequestGroup>()?.value
}

fun Request.Builder.setGroup(group: Any?) = apply {
    setLabel(NetLabel.RequestGroup(group))
}

/**
 * 请求标签
 */
fun Request.tag(name: String): Any? {
    return label<NetLabel.TagHashMap>()?.get(name)
}

/**
 * 请求标签
 */
fun Request.setTag(name: String, value: Any?) = apply {
    label<NetLabel.TagHashMap>()?.put(name, value)
}

/**
 * 设置是否记录日志
 */
fun Request.Builder.setLogRecord(enabled: Boolean) = apply {
    setLabel(NetLabel.RecordLog(enabled))
}

/**
 * 是否记录日志
 */
fun Request.isLogRecord() = run {
    label<NetLabel.RecordLog>()?.enabled
}

fun Request.Builder.setKType(type: KType) = apply {
    setLabel(type)
}

fun Request.kType(): KType? = run {
    label<KType>()
}

inline fun <reified T> Request.Builder.setLabel(any: T) = apply {
    tag(T::class.java, any)
}

inline fun <reified T> Request.label(): T? {
    return tag(T::class.java)
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
 * 上传监听器
 */
fun Request.uploadListeners(): ConcurrentLinkedQueue<ProgressListener>? {
    return label<NetLabel.UploadListeners>()
}

/**
 * 下载监听器
 */
fun Request.downloadListeners(): ConcurrentLinkedQueue<ProgressListener>? {
    return label<NetLabel.DownloadListeners>()
}

fun Request.addUploadListener(progressListener: ProgressListener) {
    uploadListeners()?.add(progressListener)
}

fun Request.addDownloadListener(progressListener: ProgressListener) {
    downloadListeners()?.add(progressListener)
}
//</editor-fold>

/**
 * 转换器
 */
fun Request.converter(): NetConverter? {
    return label<NetConverter>()
}

fun Request.Builder.setConverter(converter: NetConverter) = apply {
    setLabel(converter)
}

/**
 * 请求日志信息
 * 只会输出纯表单(form)的请求参数
 */
fun Request.logString(byteCount: Long = 1024 * 1024): String? {
    val mediaType = body?.contentType() ?: return null
    return if (body is FormBody) {
        body?.peekString(byteCount)
    } else {
        "LogRecordInterceptor not support this type $mediaType"
    }
}