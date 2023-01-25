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
import com.drake.net.convert.NetConverter
import com.drake.net.interfaces.ProgressListener
import com.drake.net.tag.NetTag
import okhttp3.OkHttpUtils
import okhttp3.Request
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KType

//<editor-fold desc="ID">
/**
 * 请求的Id
 */
var Request.id: Any?
    get() = tagOf<NetTag.RequestId>()
    set(value) {
        tagOf(value?.let { NetTag.RequestId(it) })
    }

/**
 * 请求的分组名
 * Group和Id本质上都是任意对象Any. 但是Net网络请求中自动取消的操作都是通过Group分组. 如果你覆盖可能会导致自动取消无效
 * 在设计理念上分组可以重复. Id不行
 */
var Request.group: Any?
    get() = tagOf<NetTag.RequestGroup>()
    set(value) {
        tagOf(value?.let { NetTag.RequestGroup(it) })
    }
//</editor-fold>

/**
 * KType属于Kotlin特有的Type, 某些kotlin独占框架可能会使用到. 例如 kotlin.serialization
 */
var Request.kType: KType?
    get() = tagOf<NetTag.RequestKType>()?.value
    set(value) {
        tagOf(value?.let { NetTag.RequestKType(it) })
    }


//<editor-fold desc="Extra">
/**
 * 返回键值对的标签
 * 键值对标签即OkHttp中的实际tag(在Net中叫label)中的一个Map集合
 */
fun Request.extra(name: String): Any? {
    return tagOf<NetTag.Extras>()?.get(name)
}

/**
 * 全部键值对标签
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
 * 返回OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.tagOf(): T? {
    return tag(T::class.java)
}

/**
 * 设置OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.tagOf(value: T) = apply {
    tags()[T::class.java] = value
}

/**
 * 标签集合
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
 * 当指定下载目录存在同名文件是覆盖还是进行重命名, 重命名规则是: $文件名_($序号).$后缀
 */
fun Request.downloadConflictRename(): Boolean {
    return tagOf<NetTag.DownloadFileConflictRename>()?.value == true
}

/**
 * 是否进行校验文件md5, 如果校验则匹配上既马上返回文件而不会进行下载
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
 * 临时文件命名规则: 文件名 + .net-download
 *      下载文件名: install.apk, 临时文件名: install.apk.net-download
 */
fun Request.downloadTempFile(): Boolean {
    return tagOf<NetTag.DownloadTempFile>()?.value == true
}
//</editor-fold>

/**
 * 返回请求包含的转换器
 */
fun Request.converter(): NetConverter {
    return tagOf<NetConverter>() ?: NetConfig.converter
}