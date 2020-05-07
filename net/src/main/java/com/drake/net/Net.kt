/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

@file:Suppress("unused", "FunctionName")

package com.drake.net

import android.content.Context
import com.bumptech.glide.Glide
import com.drake.net.error.ResponseException
import com.yanzhenjie.kalle.Kalle
import com.yanzhenjie.kalle.RequestMethod
import com.yanzhenjie.kalle.Url
import com.yanzhenjie.kalle.download.UrlDownload
import com.yanzhenjie.kalle.simple.SimpleBodyRequest
import com.yanzhenjie.kalle.simple.SimpleUrlRequest
import com.yanzhenjie.kalle.simple.cache.CacheMode
import kotlinx.coroutines.*
import java.io.File

// <editor-fold desc="异步请求">

/**
 * Get请求
 * @param path String 网络路径, 非绝对路径会加上HOST为前缀
 * @see NetConfig.host
 * @param absolutePath Boolean Path是否是绝对路径
 * @param tag 可以传递对象给Request请求
 * @param block SimpleUrlRequest.Api.() -> UnitUtils
 * @return Observable<M> 结果会在主线程
 */

inline fun <reified M> CoroutineScope.Get(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {
    _submitUrl<M>(RequestMethod.GET, path, tag, cache, absolutePath, block)
}


inline fun <reified M> CoroutineScope.Post(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {
    _submitBody<M>(RequestMethod.POST, path, tag, cache, absolutePath, block)
}

inline fun <reified M> CoroutineScope.Head(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {
    _submitUrl<M>(RequestMethod.HEAD, path, tag, cache, absolutePath, block)
}

inline fun <reified M> CoroutineScope.Options(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {
    _submitUrl<M>(RequestMethod.OPTIONS, path, tag, cache, absolutePath, block)
}


inline fun <reified M> CoroutineScope.Trace(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {
    _submitUrl<M>(RequestMethod.TRACE, path, tag, cache, absolutePath, block)
}

inline fun <reified M> CoroutineScope.Delete(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {
    _submitBody<M>(RequestMethod.DELETE, path, tag, cache, absolutePath, block)
}

inline fun <reified M> CoroutineScope.Put(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {
    _submitBody<M>(RequestMethod.PUT, path, tag, cache, absolutePath, block)
}

inline fun <reified M> CoroutineScope.Patch(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {
    _submitBody<M>(RequestMethod.PATCH, path, tag, cache, absolutePath, block)
}


/**
 * 下载文件
 *
 * @param path  网络路径, 非绝对路径会加上HOST[NetConfig.host]为前缀
 * @param method 请求方式, 默认GET
 * @param dir  下载文件存放目录 {默认存在android/data/packageName/cache目录}
 * @param tag 可以传递对象给Request请求
 * @param absolutePath  下载链接是否是绝对路径
 * @param block 请求参数
 */
fun CoroutineScope.Download(
    path: String,
    method: RequestMethod = RequestMethod.GET,
    dir: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    absolutePath: Boolean = false,
    block: (UrlDownload.Api.() -> Unit)? = null
): Deferred<String> = async(Dispatchers.IO) {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val download =
        UrlDownload.newApi(Url.newBuilder(realPath).build(), method).directory(dir).tag(tag)

    if (isActive) {
        if (block == null) {
            download.perform()
        } else {
            download.apply(block).perform()
        }
    } else {
        throw CancellationException()
    }
}

/**
 * 下载图片, 图片宽高要求要么同时指定要么同时不指定
 *
 * @receiver Context
 * @param url String
 * @param with Int 图片宽度
 * @param height Int 图片高度
 * @return Observable<File>
 */
fun CoroutineScope.DownloadImg(
    url: String,
    with: Int = -1,
    height: Int = -1
): Deferred<File> = async(Dispatchers.IO) {

    val download = Glide.with(NetConfig.app).download(url)

    val futureTarget = if (with == -1 && height == -1) {
        download.submit()
    } else {
        download.submit(with, height)
    }

    futureTarget.get()
}


inline fun <reified M> _submitBody(
    method: RequestMethod,
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(Url.newBuilder(realPath).build(), method)
        .tag(tag)
        .cacheKey(path)
        .cacheMode(cache)

    val response = if (block == null) {
        request.perform(M::class.java, ResponseException::class.java)
    } else {
        request.apply(block).perform<M, String>(M::class.java, ResponseException::class.java)
    }

    return if (response.isSucceed) {
        response.success!!
    } else {
        throw response.failure as ResponseException
    }
}

inline fun <reified M> _submitUrl(
    method: RequestMethod,
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), method)
        .tag(tag)
        .cacheKey(path)
        .cacheMode(cache)

    val response = if (block == null) {
        request.perform(M::class.java, ResponseException::class.java)
    } else {
        request.apply(block).perform<M, String>(M::class.java, ResponseException::class.java)
    }

    return if (response.isSucceed) {
        response.success!!
    } else {
        throw response.failure as ResponseException
    }
}

// </editor-fold>

// <editor-fold desc="同步请求">

inline fun <reified M> syncGet(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): M {
    return _submitUrl<M>(RequestMethod.GET, path, tag, cache, absolutePath, block)
}

inline fun <reified M> syncPost(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): M {
    return _submitBody<M>(RequestMethod.POST, path, tag, cache, absolutePath, block)
}

inline fun <reified M> syncHead(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): M {
    return _submitUrl<M>(RequestMethod.HEAD, path, tag, cache, absolutePath, block)
}

inline fun <reified M> syncOptions(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): M {
    return _submitUrl<M>(RequestMethod.OPTIONS, path, tag, cache, absolutePath, block)
}

inline fun <reified M> syncTrace(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): M {
    return _submitUrl<M>(RequestMethod.TRACE, path, tag, cache, absolutePath, block)
}

inline fun <reified M> syncDelete(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): M {
    return _submitBody<M>(RequestMethod.DELETE, path, tag, cache, absolutePath, block)
}

inline fun <reified M> syncPut(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): M {
    return _submitBody<M>(RequestMethod.PUT, path, tag, cache, absolutePath, block)
}

inline fun <reified M> syncPatch(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): M {
    return _submitBody<M>(RequestMethod.PATCH, path, tag, cache, absolutePath, block)
}

fun syncDownload(
    path: String,
    directory: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    absolutePath: Boolean = false,
    block: (UrlDownload.Api.() -> Unit)? = null
): String {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val download = Kalle.Download.get(realPath).directory(directory).tag(tag)

    return if (block == null) {
        download.perform()
    } else {
        download.apply(block).perform()
    }
}

fun Context.syncDownloadImg(url: String, with: Int = 0, height: Int = 0): File {

    Glide.with(this).downloadOnly()

    val download = Glide.with(this).download(url)

    val futureTarget = if (with == 0 && height == 0) {
        download.submit()
    } else {
        download.submit(with, height)
    }

    return futureTarget.get()
}

// </editor-fold>


