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

package com.drake.net.request

import com.drake.net.NetConfig
import com.drake.net.cache.CacheMode
import com.drake.net.cache.ForceCache
import com.drake.net.convert.NetConverter
import com.drake.net.exception.URLParseException
import com.drake.net.interfaces.ProgressListener
import com.drake.net.okhttp.toNetOkhttp
import com.drake.net.reflect.TypeToken
import com.drake.net.reflect.typeTokenOf
import com.drake.net.response.convert
import com.drake.net.tag.NetTag
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.File
import java.lang.reflect.Type
import java.net.URL
import java.util.concurrent.TimeUnit
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
            val forceCache = field.cache?.let { ForceCache(OkHttpUtils.diskLruCache(it)) }
            tagOf(forceCache)
        }

    /**
     * 修改当前Request的OkHttpClient配置, 不会影响全局默认的OkHttpClient
     */
    fun setClient(block: OkHttpClient.Builder.() -> Unit) {
        okHttpClient = okHttpClient.newBuilder().apply(block).toNetOkhttp().build()
    }
    //</editor-fold>

    //<editor-fold desc="ID">
    /**
     * 请求ID
     * Group和Id在使用场景上有所区别, 预期上Group允许重复赋值给多个请求, Id仅允许赋值给一个请求, 但实际上都允许重复赋值
     * 在作用域中发起请求时会默认使用协程错误处理器作为Group: `setGroup(coroutineContext[CoroutineExceptionHandler])`
     * 如果你覆盖Group会导致协程结束不会自动取消请求
     */
    fun setId(id: Any?) {
        okHttpRequest.id = id
    }

    /**
     * 请求分组
     * Group和Id在使用场景上有所区别, 预期上Group允许重复赋值给多个请求, Id仅允许赋值给一个请求, 但实际上都允许重复赋值
     * 在作用域中发起请求时会默认使用协程错误处理器作为Group: `setGroup(coroutineContext[CoroutineExceptionHandler])`
     * 如果你覆盖Group会导致协程结束不会自动取消请求
     */
    fun setGroup(group: Any?) {
        okHttpRequest.group = group
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
        } else {
            httpUrl.setQueryParameter(name, value)
        }
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

    /**
     * 添加Url上的Query参数
     */
    fun addQuery(name: String, value: String?, encoded: Boolean = false) {
        if (encoded) {
            httpUrl.addEncodedQueryParameter(name, value)
        } else {
            httpUrl.addQueryParameter(name, value)
        }
    }

    /**
     * 添加Url上的Query参数
     */
    fun addQuery(name: String, value: Number?) {
        addQuery(name, value?.toString() ?: return)
    }

    /**
     * 添加Url上的Query参数
     */
    fun addQuery(name: String, value: Boolean?) {
        addQuery(name, value?.toString() ?: return)
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

    //<editor-fold desc="Extra">
    /**
     * 设置额外信息
     * @see extra 读取
     * @see extras 全部额外信息
     */
    fun setExtra(name: String, tag: Any?) {
        okHttpRequest.setExtra(name, tag)
    }

    //</editor-fold>

    //<editor-fold desc="Tag">

    /**
     * 使用Any::class作为键名添加标签
     * 使用Request.tag()返回tag
     */
    fun tag(tag: Any?) {
        okHttpRequest.tag(tag)
    }

    /**
     * 使用[type]作为key添加标签
     * 使用Request.tagOf<T>()或者Request.tag(Class)读取tag
     */
    fun <T> tag(type: Class<in T>, tag: T?) {
        okHttpRequest.tag(type, tag)
    }

    /**
     * 添加tag
     * 使用Request.tagOf<T>()或者Request.tag(Class)读取tag
     */
    inline fun <reified T> tagOf(tag: T?) {
        okHttpRequest.tagOf(tag)
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

    /**
     * 全部请求头
     */
    fun headers(): Headers.Builder {
        return okHttpRequest.headers()
    }

    //</editor-fold>

    //<editor-fold desc="Cache">

    /**
     * 设置Http缓存协议头的缓存控制
     */
    fun setCacheControl(cacheControl: CacheControl) {
        okHttpRequest.cacheControl(cacheControl)
    }

    /**
     * 设置缓存模式
     * 缓存模式将无视Http缓存协议进行强制读取/写入缓存
     */
    fun setCacheMode(mode: CacheMode) {
        tagOf(mode)
    }

    /**
     * 自定义强制缓存使用的Key, 本方法对于Http缓存协议无效
     * @param key 缓存的Key无论是自定义还是默认(使用RequestMethod+URL作为Key)最终都会被进行SHA1编码, 所以无需考虑特殊字符问题
     */
    fun setCacheKey(key: String) {
        tagOf(NetTag.CacheKey(key))
    }

    /**
     * 强制缓存有效期
     * 注意即使缓存有效期很长也无法阻止LRU最近最少使用算法清除超出缓存最大限制
     *
     * 标准Http缓存协议遵守协议本身的有效期, 当前方法配置无效
     * @param duration 持续时间
     * @param unit 时间单位, 默认毫秒
     */
    fun setCacheValidTime(duration: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
        tagOf(NetTag.CacheValidTime(unit.toMillis(duration)))
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
    fun setDownloadFileName(name: String) {
        okHttpRequest.tagOf(NetTag.DownloadFileName(name))
    }

    /**
     * 下载保存的目录, 也支持包含文件名称的完整路径, 如果使用完整路径则无视setDownloadFileName设置
     */
    fun setDownloadDir(name: String) {
        okHttpRequest.tagOf(NetTag.DownloadFileDir(name))
    }

    /**
     * 下载保存的目录, 也支持包含文件名称的完整路径, 如果使用完整路径则无视setDownloadFileName设置
     */
    fun setDownloadDir(name: File) {
        okHttpRequest.tagOf(NetTag.DownloadFileDir(name))
    }

    /**
     * 下载文件MD5校验
     * 如果服务器响应头`Content-MD5`值和指定路径已经存在的文件MD5相同, 则跳过下载直接返回该File
     */
    fun setDownloadMd5Verify(enabled: Boolean = true) {
        okHttpRequest.tagOf(NetTag.DownloadFileMD5Verify(enabled))
    }

    /**
     * 下载文件路径存在同名文件时是创建新文件(添加序号)还是覆盖
     * 重命名规则是: $文件名_($序号).$后缀, 例如`file_name(1).apk`
     */
    fun setDownloadFileNameConflict(enabled: Boolean = true) {
        okHttpRequest.tagOf(NetTag.DownloadFileConflictRename(enabled))
    }

    /**
     * 文件名称是否使用URL解码
     * 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称
     */
    fun setDownloadFileNameDecode(enabled: Boolean = true) {
        okHttpRequest.tagOf(NetTag.DownloadFileNameDecode(enabled))
    }

    /**
     * 下载是否使用临时文件
     * 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名
     * 临时文件命名规则: 文件名 + .downloading
     *      下载文件名: install.apk, 临时文件名: install.apk.downloading
     */
    fun setDownloadTempFile(enabled: Boolean = true) {
        okHttpRequest.tagOf(NetTag.DownloadTempFile(enabled))
    }

    /**
     * 下载时设置断点续传的范围
     */
    fun setDownloadPartialRange(startRange: Long = 0, endRange: Long = 0) {
        addHeader(
            "Range",
            "bytes=${if (startRange >= 0) startRange else ""}-${if (endRange >= 0 && endRange > startRange) endRange else ""}"
        )
    }

    /**
     * 下载监听器
     */
    fun addDownloadListener(progressListener: ProgressListener) {
        okHttpRequest.downloadListeners().add(progressListener)
    }

    //</editor-fold>

    /**
     * 为请求附着KType信息
     * KType属于Kotlin特有的Type, 某些Kotlin框架可能会使用到, 例如 kotlin.serialization
     */
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> setKType() {
        okHttpRequest.kType = typeOf<T>()
    }

    /**
     * 构建请求对象Request
     */
    open fun buildRequest(): Request {
        return okHttpRequest.method(method.name, null).url(httpUrl.build()).setConverter(converter)
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
        return newCall.execute().convert()
    }

    /**
     * 执行同步请求
     * 本方法仅为兼容Java使用存在
     * @param type 如果存在泛型嵌套要求使用[typeTokenOf]或者[TypeToken]获取, 否则泛型会被擦除导致无法解析
     */
    fun <R> execute(type: Type): R {
        NetConfig.requestInterceptor?.interceptor(this)
        val request = buildRequest()
        val newCall = okHttpClient.newCall(request)
        return newCall.execute().convert(type)
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
            val value = newCall.execute().convert<R>()
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
        newCall.enqueue(block)
        return newCall
    }
    //</editor-fold>
}

