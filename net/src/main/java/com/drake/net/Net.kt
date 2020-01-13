/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

@file:Suppress("unused")

package com.drake.net

import android.content.Context
import com.bumptech.glide.Glide
import com.drake.net.error.ResponseException
import com.yanzhenjie.kalle.Kalle
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

inline fun <reified M> CoroutineScope.get(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {

    val request = Kalle.get(if (absolutePath) path else (NetConfig.host + path))
        .tag(tag)
        .cacheKey(path)
        .cacheMode(cache)

    val response = if (block == null) {
        request.perform(M::class.java, ResponseException::class.java)
    } else {
        request.apply(block).perform<M, String>(M::class.java, ResponseException::class.java)
    }

    if (response.isSucceed) {
        response.succeed()
    } else {
        throw response.failed() as ResponseException
    }
}


inline fun <reified M> CoroutineScope.post(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): Deferred<M> = async(Dispatchers.IO) {

    val request =
        Kalle.post(if (absolutePath) path else (NetConfig.host + path))
            .tag(tag)
            .cacheKey(path)
            .cacheMode(cache)

    val response = if (block == null) {
        request.perform<M, String>(M::class.java, ResponseException::class.java)
    } else {
        request.apply(block).perform<M, String>(M::class.java, ResponseException::class.java)
    }

    if (response.isSucceed) {
        response.succeed()
    } else {
        throw response.failed() as ResponseException
    }
}


/**
 * 下载文件
 *
 * @param path String 网络路径, 非绝对路径会加上HOST[NetConfig.host]为前缀
 * @param directory String 下载文件存放目录 {默认存在android/data/packageName/cache目录}
 * @param tag 可以传递对象给Request请求
 * @param absolutePath Boolean 下载链接是否是绝对路径
 * @param block 请求参数
 * @return Observable<String> 结果会在主线程
 */
fun CoroutineScope.download(
    path: String,
    directory: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    absolutePath: Boolean = false,
    block: (UrlDownload.Api.() -> Unit)? = null
): Deferred<String> = async(Dispatchers.IO) {
    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val download = Kalle.Download.get(realPath).directory(directory).tag(tag)

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
fun CoroutineScope.downImage(
    context: Context,
    url: String,
    with: Int = -1,
    height: Int = -1
): Deferred<File> = async(Dispatchers.IO) {

    Glide.with(context).downloadOnly()

    val download = Glide.with(context).download(url)

    val futureTarget = if (with == -1 && height == -1) {
        download.submit()
    } else {
        download.submit(with, height)
    }

    futureTarget.get()
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

    val request =
        Kalle.get(if (absolutePath) path else (NetConfig.host + path)).tag(tag).cacheKey(path)
            .cacheMode(cache)
    val response = if (block == null) {
        request.perform(M::class.java, ResponseException::class.java)
    } else {
        request.apply(block).perform<M, String>(M::class.java, ResponseException::class.java)
    }

    return if (response.isSucceed) {
        response.succeed()
    } else {
        throw response.failed() as ResponseException
    }
}

inline fun <reified M> syncPost(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): M {

    val request =
        Kalle.post(if (absolutePath) path else (NetConfig.host + path)).tag(tag).cacheKey(path)
            .cacheMode(cache)
    val response = if (block == null) {
        request.perform<M, String>(M::class.java, ResponseException::class.java)
    } else {
        request.apply(block).perform<M, String>(M::class.java, ResponseException::class.java)
    }

    return if (response.isSucceed) {
        response.succeed()
    } else {
        throw response.failed() as ResponseException
    }
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

fun Context.syncDownImage(url: String, with: Int = 0, height: Int = 0): File {

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


