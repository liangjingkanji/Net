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

@file:Suppress("unused", "FunctionName")

package com.drake.net

import android.content.Context
import com.bumptech.glide.Glide
import com.yanzhenjie.kalle.NetCancel
import com.yanzhenjie.kalle.RequestMethod
import com.yanzhenjie.kalle.Url
import com.yanzhenjie.kalle.download.BodyDownload
import com.yanzhenjie.kalle.download.UrlDownload
import com.yanzhenjie.kalle.simple.SimpleBodyRequest
import com.yanzhenjie.kalle.simple.SimpleUrlRequest
import com.yanzhenjie.kalle.simple.cache.CacheMode
import kotlinx.coroutines.*
import java.io.File
import java.net.SocketException

// <editor-fold desc="异步请求">


/**
 * 异步网络请求
 *
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Get(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    noinline block: SimpleUrlRequest.Api.() -> Unit = {}
): Deferred<M> = async(Dispatchers.IO + SupervisorJob()) {
    if (!isActive) throw CancellationException()

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.GET)
            .tag(tag)
            .uid(uid)
            .cacheKey(path)
            .cacheMode(cache)

    try {
        request.apply(block).perform<M>(M::class.java)
    } catch (e: SocketException) {
        throw CancellationException()
    }
}


/**
 * 异步网络请求
 *
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Post(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    noinline block: SimpleBodyRequest.Api.() -> Unit = {}
): Deferred<M> = async(Dispatchers.IO + SupervisorJob()) {
    if (!isActive) throw CancellationException()

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.POST)
            .tag(tag)
            .uid(uid)
            .cacheKey(path)
            .cacheMode(cache)

    try {
        request.apply(block).perform<M>(M::class.java)
    } catch (e: SocketException) {
        throw CancellationException()
    }
}


/**
 * 异步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Head(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    noinline block: SimpleUrlRequest.Api.() -> Unit = {}
): Deferred<M> = async(Dispatchers.IO + SupervisorJob()) {
    if (!isActive) throw CancellationException()

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.HEAD)
            .tag(tag)
            .uid(uid)
            .cacheKey(path)
            .cacheMode(cache)

    try {
        request.apply(block).perform<M>(M::class.java)
    } catch (e: SocketException) {
        throw CancellationException()
    }
}

/**
 * 异步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Options(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    noinline block: SimpleUrlRequest.Api.() -> Unit = {}
): Deferred<M> = async(Dispatchers.IO + SupervisorJob()) {
    if (!isActive) throw CancellationException()

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.OPTIONS)
            .tag(tag)
            .uid(uid)
            .cacheKey(path)
            .cacheMode(cache)

    try {
        request.apply(block).perform<M>(M::class.java)
    } catch (e: SocketException) {
        throw CancellationException()
    }
}

/**
 * 异步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Trace(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    noinline block: SimpleUrlRequest.Api.() -> Unit = {}
): Deferred<M> = async(Dispatchers.IO + SupervisorJob()) {
    if (!isActive) throw CancellationException()

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.TRACE)
            .tag(tag)
            .uid(uid)
            .cacheKey(path)
            .cacheMode(cache)

    try {
        request.apply(block).perform<M>(M::class.java)
    } catch (e: SocketException) {
        throw CancellationException()
    }
}

/**
 * 异步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Delete(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    noinline block: SimpleBodyRequest.Api.() -> Unit = {}
): Deferred<M> = async(Dispatchers.IO + SupervisorJob()) {
    if (!isActive) throw CancellationException()

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.DELETE)
            .tag(tag)
            .uid(uid)
            .cacheKey(path)
            .cacheMode(cache)

    try {
        request.apply(block).perform<M>(M::class.java)
    } catch (e: SocketException) {
        throw CancellationException()
    }
}

/**
 * 异步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Put(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    noinline block: SimpleBodyRequest.Api.() -> Unit = {}
): Deferred<M> = async(Dispatchers.IO + SupervisorJob()) {
    if (!isActive) throw CancellationException()

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(
            Url.newBuilder(realPath).build(),
            RequestMethod.PUT
                                          )
            .tag(tag)
            .uid(uid)
            .cacheKey(path)
            .cacheMode(cache)

    try {
        request.apply(block).perform<M>(M::class.java)
    } catch (e: SocketException) {
        throw CancellationException()
    }
}

/**
 * 异步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Patch(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    noinline block: SimpleBodyRequest.Api.() -> Unit = {}
): Deferred<M> = async(Dispatchers.IO + SupervisorJob()) {
    if (!isActive) throw CancellationException()

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.PATCH)
            .tag(tag)
            .uid(uid)
            .cacheKey(path)
            .cacheMode(cache)

    try {
        request.apply(block).perform<M>(M::class.java)
    } catch (e: SocketException) {
        throw CancellationException()
    }
}

/**
 * 用于提交URL体下载文件(默认GET请求)
 *
 * @param path  请求路径, 非绝对路径会加上HOST[NetConfig.host]为前缀
 * @param method 请求方式, 默认GET
 * @param dir  下载文件存放目录 {默认存在android/data/packageName/cache目录}
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath  下载链接是否是绝对路径
 * @param block 请求参数
 */
fun CoroutineScope.Download(
    path: String,
    dir: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    absolutePath: Boolean = false,
    method: RequestMethod = RequestMethod.GET,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    block: UrlDownload.Api.() -> Unit = {}
): Deferred<String> = async(Dispatchers.IO + SupervisorJob()) {

    if (!isActive) throw CancellationException()

    if (method == RequestMethod.POST || method == RequestMethod.DELETE || method == RequestMethod.PUT || method == RequestMethod.PATCH) {
        throw UnsupportedOperationException("You should use [DownloadBody] function")
    }

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val download =
        UrlDownload.newApi(Url.newBuilder(realPath).build(), method).directory(dir).tag(tag)
            .uid(uid)

    try {
        download.apply(block).perform()
    } catch (e: SocketException) {
        throw CancellationException()
    }
}

/**
 * 用于提交请求体下载文件(默认POST请求)
 *
 * @param path  请求路径, 非绝对路径会加上HOST[NetConfig.host]为前缀
 * @param method 请求方式, 默认GET
 * @param dir  下载文件存放目录 {默认存在android/data/packageName/cache目录}
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath  下载链接是否是绝对路径
 * @param block 配置请求参数
 */
fun CoroutineScope.DownloadBody(
    path: String,
    dir: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    absolutePath: Boolean = false,
    method: RequestMethod = RequestMethod.POST,
    uid: Any? = coroutineContext[CoroutineExceptionHandler],
    block: BodyDownload.Api.() -> Unit = {}
): Deferred<String> = async(Dispatchers.IO + SupervisorJob()) {

    if (!isActive) throw CancellationException()

    if (method == RequestMethod.GET || method == RequestMethod.HEAD || method == RequestMethod.OPTIONS || method == RequestMethod.TRACE) {
        throw UnsupportedOperationException("You should use [Download] function")
    }

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val download =
        BodyDownload.newApi(Url.newBuilder(realPath).build(), method).directory(dir).tag(tag)
            .uid(uid)

    try {
        download.apply(block).perform()
    } catch (e: SocketException) {
        throw CancellationException()
    }
}

/**
 * 异步下载图片, 图片宽高要求要么同时指定要么同时不指定
 * 要求依赖 Glide
 *
 * @param url 请求图片的绝对路径
 * @param with 图片宽度
 * @param height 图片高度
 */
fun CoroutineScope.DownloadImage(url: String, with: Int = -1, height: Int = -1): Deferred<File> =
    async(Dispatchers.IO + SupervisorJob()) {

        val download = Glide.with(NetConfig.app).download(url)

        val futureTarget = if (with == -1 && height == -1) {
            download.submit()
        } else {
            download.submit(with, height)
        }

        futureTarget.get()
    }

// </editor-fold>

// <editor-fold desc="同步请求">

/**
 * 同步网络请求
 *
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> syncGet(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = null,
    noinline block: SimpleUrlRequest.Api.() -> Unit = {}
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.GET)
        .tag(tag)
        .uid(uid)
        .cacheKey(path)
        .cacheMode(cache)

    return request.apply(block).perform(M::class.java)
}

/**
 * 同步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> syncPost(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = null,
    noinline block: SimpleBodyRequest.Api.() -> Unit = {}
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.POST)
        .tag(tag)
        .uid(uid)
        .cacheKey(path)
        .cacheMode(cache)

    return request.apply(block).perform(M::class.java)
}

/**
 * 同步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> syncHead(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = null,
    noinline block: SimpleUrlRequest.Api.() -> Unit = {}
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.HEAD)
        .tag(tag)
        .uid(uid)
        .cacheKey(path)
        .cacheMode(cache)

    return request.apply(block).perform(M::class.java)
}

/**
 * 同步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> syncOptions(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = null,
    noinline block: SimpleUrlRequest.Api.() -> Unit = {}
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.OPTIONS)
        .tag(tag)
        .uid(uid)
        .cacheKey(path)
        .cacheMode(cache)

    return request.apply(block).perform(M::class.java)
}

/**
 * 同步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> syncTrace(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = null,
    noinline block: SimpleUrlRequest.Api.() -> Unit = {}
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleUrlRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.TRACE)
        .tag(tag)
        .uid(uid)
        .cacheKey(path)
        .cacheMode(cache)

    return request.apply(block).perform(M::class.java)
}

/**
 * 同步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> syncDelete(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = null,
    noinline block: SimpleBodyRequest.Api.() -> Unit = {}
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.DELETE)
        .tag(tag)
        .uid(uid)
        .cacheKey(path)
        .cacheMode(cache)

    return request.apply(block).perform(M::class.java)
}

/**
 * 同步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> syncPut(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = null,
    noinline block: SimpleBodyRequest.Api.() -> Unit = {}
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.PUT)
        .tag(tag)
        .uid(uid)
        .cacheKey(path)
        .cacheMode(cache)

    return request.apply(block).perform(M::class.java)
}

/**
 * 同步网络请求
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> syncPatch(
    path: String,
    tag: Any? = null,
    cache: CacheMode = CacheMode.HTTP,
    absolutePath: Boolean = false,
    uid: Any? = null,
    noinline block: SimpleBodyRequest.Api.() -> Unit = {}
): M {

    val realPath = if (absolutePath) path else (NetConfig.host + path)

    val request = SimpleBodyRequest.newApi(Url.newBuilder(realPath).build(), RequestMethod.PATCH)
        .tag(tag)
        .uid(uid)
        .cacheKey(path)
        .cacheMode(cache)

    return request.apply(block).perform(M::class.java)
}

/**
 * 同步文件下载, 默认Get请求
 *
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
fun syncDownload(
    path: String,
    dir: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    absolutePath: Boolean = false,
    method: RequestMethod = RequestMethod.GET,
    uid: Any? = null,
    block: UrlDownload.Api.() -> Unit = {}
): String {
    if (method == RequestMethod.POST || method == RequestMethod.DELETE || method == RequestMethod.PUT || method == RequestMethod.PATCH) {
        throw UnsupportedOperationException("You should use [syncDownloadBody] function")
    }
    val realPath = if (absolutePath) path else (NetConfig.host + path)
    val download =
        UrlDownload.newApi(Url.newBuilder(realPath).build(), method).directory(dir).tag(tag)
            .uid(uid)
    return download.apply(block).perform()
}

/**
 * 同步文件下载, 允许提交请求体方式, 默认Post请求
 *
 * @param path String 请求路径, 非绝对路径会加上[NetConfig.host]为前缀
 * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param absolutePath 请求路径是否是绝对路径
 * @param uid 表示请求的唯一id, 和[NetCancel.cancel]中的uid一致, 一般用于指定取消网络请求的唯一id
 * @param block 函数中可以配置请求参数
 */
fun syncDownloadBody(
    path: String,
    dir: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    absolutePath: Boolean = false,
    method: RequestMethod = RequestMethod.GET,
    uid: Any? = null,
    block: BodyDownload.Api.() -> Unit = {}
): String {
    if (method == RequestMethod.GET || method == RequestMethod.HEAD || method == RequestMethod.OPTIONS || method == RequestMethod.TRACE) {
        throw UnsupportedOperationException("You should use [syncDownload] function")
    }
    val realPath = if (absolutePath) path else (NetConfig.host + path)
    val download =
        BodyDownload.newApi(Url.newBuilder(realPath).build(), method).directory(dir).tag(tag)
            .uid(uid)
    return download.apply(block).perform()
}


/**
 * 同步下载图片, 图片宽高要求要么同时指定要么同时不指定
 * 要求依赖 Glide
 *
 * @param url 请求图片的绝对路径
 * @param with 图片宽度
 * @param height 图片高度
 */
fun Context.syncDownloadImage(url: String, with: Int = 0, height: Int = 0): File {

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




