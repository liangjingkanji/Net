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
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING", "RedundantSetter")

package com.drake.net.request

import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.exception.URLParseException
import com.drake.net.interfaces.NetCallback
import com.drake.net.interfaces.ProgressListener
import com.drake.net.okhttp.toNetOkhttp
import com.drake.net.response.convert
import com.drake.net.tag.NetLabel
import com.drake.net.utils.runMain
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.concurrent.CancellationException
import kotlin.reflect.typeOf

abstract class BaseRequest {

    /** 请求的Url构造器 */
    open var httpUrl: HttpUrl.Builder = HttpUrl.Builder()

    /** 当前请求的数据转换器 */
    open var converter: NetConverter = NetConfig.converter

    /** 请求的方法 */
    open var method = Method.GET

    //<editor-fold desc="OkHttpClient" >

    /** 请求对象构造器 */
    open var okHttpRequest: Request.Builder = Request.Builder()

    /** 请求客户端 */
    open var okHttpClient = NetConfig.okHttpClient
        set(value) {
            field = value.toNetOkhttp()
        }

    /**
     * 修改当前Request的OkHttpClient配置, 不会影响全局默认的OkHttpClient
     */
    fun setClient(block: OkHttpClient.Builder.() -> Unit) {
        okHttpClient = okHttpClient.newBuilder().apply(block).toNetOkhttp().build()
    }
    //</editor-fold>

    //<editor-fold desc="URL">

    /**
     * 设置一个Url字符串, 其参数不会和你初始化时设置的主域名[NetConfig.host]进行拼接
     * 一般情况下我建议使用更为聪明的[setPath]
     */
    open fun setUrl(url: String) {
        try {
            httpUrl = url.toHttpUrl().newBuilder()
        } catch (e: Exception) {
            throw URLParseException(url, e)
        }
    }

    /**
     * 设置Url
     */
    open fun setUrl(url: HttpUrl) {
        httpUrl = url.newBuilder()
    }

    /**
     * 设置Url
     */
    open fun setUrl(url: URL) {
        setUrl(url.toString())
    }

    /**
     * 解析配置Path, 支持识别query参数和绝对路径
     * @param path 如果其不包含http/https则会自动拼接[NetConfig.host]
     */
    fun setPath(path: String?) {
        val url = path?.toHttpUrlOrNull()
        if (url == null) {
            try {
                httpUrl = (NetConfig.host + path).toHttpUrl().newBuilder()
            } catch (e: Throwable) {
                throw URLParseException(NetConfig.host + path, e)
            }
        } else {
            this.httpUrl = url.newBuilder()
        }
    }

    /**
     * 设置Url上的Query参数
     */
    fun setQuery(name: String, value: String?, encoded: Boolean = false) {
        if (encoded) {
            httpUrl.setEncodedQueryParameter(name, value)
        } else httpUrl.setQueryParameter(name, value)
    }

    /**
     * 设置Url上的Query参数
     */
    fun setQuery(name: String, value: Number?) {
        setQuery(name, value?.toString() ?: return)
    }

    /**
     * 设置Url上的Query参数
     */
    fun setQuery(name: String, value: Boolean?) {
        setQuery(name, value?.toString() ?: return)
    }

    //</editor-fold>

    //<editor-fold desc="Param">

    /**
     * 基础类型表单参数
     *
     * 如果当前请求为Url请求则为Query参数
     * 如果当前请求为表单请求则为表单参数
     * 如果当前为Multipart包含流/文件的请求则为multipart参数
     */
    abstract fun param(name: String, value: String?)

    /**
     * 基础类型表单参数
     *
     * 如果当前请求为Url请求则为Query参数
     * 如果当前请求为表单请求则为表单参数
     * 如果当前为Multipart包含流/文件的请求则为multipart参数
     *
     * @param encoded 对应OkHttp参数函数中的encoded表示当前字段参数已经编码过. 不会再被自动编码
     */
    abstract fun param(name: String, value: String?, encoded: Boolean)

    /**
     * 基础类型表单参数
     *
     * 如果当前请求为Url请求则为Query参数
     * 如果当前请求为表单请求则为表单参数
     * 如果当前为Multipart包含流/文件的请求则为multipart参数
     */
    abstract fun param(name: String, value: Number?)

    /**
     * 基础类型表单参数
     *
     * 如果当前请求为Url请求则为Query参数
     * 如果当前请求为表单请求则为表单参数
     * 如果当前为Multipart包含流/文件的请求则为multipart参数
     */
    abstract fun param(name: String, value: Boolean?)

    //</editor-fold>

    //<editor-fold desc="Tag">

    /**
     * 唯一的Id
     */
    fun setId(id: Any?) {
        okHttpRequest.setId(id)
    }

    /**
     * 分组
     */
    fun setGroup(group: Any?) {
        okHttpRequest.setGroup(group)
    }

    /**
     * 使用Any::class作为键名添加标签
     * 使用Request.tag()返回标签
     */
    fun setTag(tag: Any?) {
        okHttpRequest.tag(tag)
    }

    /**
     * 使用[type]作为键名添加标签
     * 使用Request.label<T>()或者Request.tag(type)返回标签
     */
    fun <T> setTag(type: Class<in T>, tag: T?) {
        okHttpRequest.tag(type, tag)
    }

    /**
     * 使用[T]作为键名添加标签
     * 使用Request.label<T>()或者Request.tag(type)返回标签
     */
    inline fun <reified T> setLabel(tag: T?) {
        okHttpRequest.setLabel(tag)
    }

    /**
     * 添加标签
     * 使用`Request.tag(name)`得到指定标签
     *
     * @param name 标签名称
     * @param tag 标签
     */
    fun setTag(name: String, tag: Any?) {
        okHttpRequest.setTag(name, tag)
    }

    /**
     * 为请求附着针对Kotlin的Type信息
     */
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> setKType() {
        okHttpRequest.setKType(typeOf<T>())
    }

    //</editor-fold>

    //<editor-fold desc="Header">

    /**
     * 添加请求头
     * 如果已存在相同`name`的请求头会添加而不会覆盖, 因为请求头本身存在多个值
     */
    fun addHeader(name: String, value: String) {
        okHttpRequest.addHeader(name, value)
    }

    /**
     * 设置请求头, 会覆盖请求头而不像[addHeader]是添加
     */
    fun setHeader(name: String, value: String) {
        okHttpRequest.header(name, value)
    }

    /**
     * 删除请求头
     */
    fun removeHeader(name: String) {
        okHttpRequest.removeHeader(name)
    }

    /**
     * 批量设置请求头
     */
    fun setHeaders(headers: Headers) {
        okHttpRequest.headers(headers)
    }

    //</editor-fold>

    //<editor-fold desc="Cache">

    /**
     * 设置请求头的缓存控制
     */
    fun setCacheControl(cacheControl: CacheControl) {
        okHttpRequest.cacheControl(cacheControl)
    }
    //</editor-fold>

    //<editor-fold desc="Download">

    /**
     * 下载文件名
     * 如果[setDownloadDir]函数使用完整路径(包含文件名的参数)作为参数则将无视本函数设置
     * 如果不调用本函数则默认是读取服务器返回的文件名
     * @see setDownloadFileNameDecode
     * @see setDownloadFileNameConflict
     * @see setDownloadDir
     */
    fun setDownloadFileName(name: String?) {
        okHttpRequest.setLabel(NetLabel.DownloadFileName(name))
    }

    /**
     * 下载保存的目录, 也支持包含文件名称的完整路径, 如果使用完整路径则无视setDownloadFileName设置
     */
    fun setDownloadDir(name: String?) {
        okHttpRequest.setLabel(NetLabel.DownloadFileDir(name))
    }

    /**
     * 下载保存的目录, 也支持包含文件名称的完整路径, 如果使用完整路径则无视setDownloadFileName设置
     */
    fun setDownloadDir(name: File?) {
        okHttpRequest.setLabel(NetLabel.DownloadFileDir(name))
    }

    /**
     * 如果服务器返回 "Content-MD5"响应头和制定路径已经存在的文件MD5相同是否直接返回File
     */
    fun setDownloadMd5Verify(enabled: Boolean = true) {
        okHttpRequest.setLabel(NetLabel.DownloadFileMD5Verify(enabled))
    }

    /**
     * 假设下载文件路径已存在同名文件是否重命名, 例如`file_name(1).apk`
     */
    fun setDownloadFileNameConflict(enabled: Boolean = true) {
        okHttpRequest.setLabel(NetLabel.DownloadFileConflictRename(enabled))
    }

    /**
     * 文件名称是否使用URL解码
     * 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称
     */
    fun setDownloadFileNameDecode(enabled: Boolean = true) {
        okHttpRequest.setLabel(NetLabel.DownloadFileNameDecode(enabled))
    }

    /**
     * 下载是否使用临时文件
     * 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名
     * 临时文件命名规则: 文件名 + .net-download
     *      下载文件名: install.apk, 临时文件名: install.apk.net-download
     */
    fun setDownloadTempFile(enabled: Boolean = true) {
        okHttpRequest.setLabel(NetLabel.DownloadTempFile(enabled))
    }

    /**
     * 下载监听器
     */
    fun addDownloadListener(progressListener: ProgressListener) {
        okHttpRequest.downloadListeners().add(progressListener)
    }

    //</editor-fold>

    /**
     * 是否启用日志记录器
     */
    fun setLogRecord(enabled: Boolean) {
        okHttpRequest.setLogRecord(enabled)
    }

    /**
     * 构建请求对象Request
     */
    open fun buildRequest(): Request {
        return okHttpRequest.method(method.name, null)
            .url(httpUrl.build())
            .setConverter(converter)
            .build()
    }

    //<editor-fold desc="SyncRequest">
    /**
     * 执行同步请求
     */
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R> execute(): R {
        NetConfig.requestInterceptor?.interceptor(this)
        setKType<R>()
        val request = buildRequest()
        val newCall = okHttpClient.newCall(request)
        return newCall.execute().convert(converter)
    }

    /**
     * 执行同步请求
     * @return 一个包含请求成功和错误的Result
     */
    inline fun <reified R> toResult(): Result<R> {
        NetConfig.requestInterceptor?.interceptor(this)
        setKType<R>()
        val request = buildRequest()
        val newCall = okHttpClient.newCall(request)
        return try {
            val value = newCall.execute().convert<R>(converter)
            Result.success(value)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //</editor-fold>

    //<editor-fold desc="EnqueueRequest">
    /**
     * 队列请求. 支持OkHttp的Callback函数组件
     */
    fun enqueue(block: Callback): Call {
        NetConfig.requestInterceptor?.interceptor(this)
        val request = buildRequest()
        val newCall = okHttpClient.newCall(request)
        if (block is NetCallback<*>) {
            block.request = request
            block.onStart(request)
        }
        newCall.enqueue(block)
        return newCall
    }

    /**
     * 队列请求. 支持Result作为请求结果
     */
    inline fun <reified R> onResult(crossinline block: Result<R>.() -> Unit): Call {
        NetConfig.requestInterceptor?.interceptor(this)
        val newCall = okHttpClient.newCall(buildRequest())
        setKType<R>()
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val message = e.cause?.message
                val exception = if (message == "Socket closed") {
                    CancellationException(message)
                } else e
                runMain { block(Result.failure(exception)) }
            }

            override fun onResponse(call: Call, response: Response) {
                val success = try {
                    response.convert<R>(converter)
                } catch (e: IOException) {
                    onFailure(call, e)
                    return
                }
                runMain { block(Result.success(success)) }
            }
        })
        return newCall
    }
    //</editor-fold>
}

