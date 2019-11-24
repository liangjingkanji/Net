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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

// <editor-fold desc="异步请求">

/**
 * Get请求
 * @param path String 网络路径, 非绝对路径会加上HOST为前缀
 * @see NetConfig.host
 * @param isAbsolutePath Boolean Path是否是绝对路径
 * @param tag 可以传递对象给Request请求
 * @param block SimpleUrlRequest.Api.() -> UnitUtils
 * @return Observable<M> 结果会在主线程
 */
inline fun <reified M> get(
    path: String,
    tag: Any? = null,
    isAbsolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): Observable<M> {

    return Observable.create<M> {
        try {
            val get = Kalle.get(if (isAbsolutePath) path else (NetConfig.host + path)).tag(tag)
            val response = if (block == null) {
                get.perform(M::class.java, String::class.java)
            } else {
                get.apply(block).perform<M, String>(M::class.java, String::class.java)
            }

            if (!it.isDisposed) {
                if (response.isSucceed) {
                    it.onNext(response.succeed())
                    it.onComplete()
                } else {
                    it.onError(
                        ResponseException(response.failed(), response.code())
                    )
                }
            }
        } catch (e: java.lang.Exception) {
            if (!it.isDisposed) {
                it.onError(e)
            }
        }
    }.onTerminateDetach().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * Post提交
 * @param path String 网络路径, 非绝对路径会加上HOST为前缀
 * @see NetConfig.host
 * @param tag 可以传递对象给Request请求
 * @param isAbsolutePath Boolean 是否是绝对路径
 * @param block SimpleBodyRequest.Api.() -> UnitUtils
 * @return Observable<M> 结果会在主线程
 */
inline fun <reified M> post(
    path: String,
    tag: Any? = null,
    isAbsolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): Observable<M> {

    return Observable.create<M> {
        try {
            val post = Kalle.post(if (isAbsolutePath) path else (NetConfig.host + path)).tag(tag)
            val response = if (block == null) {
                post.perform<M, String>(M::class.java, String::class.java)
            } else {
                post.apply(block).perform<M, String>(M::class.java, String::class.java)
            }

            if (!it.isDisposed) {
                if (response.isSucceed) {
                    it.onNext(response.succeed())
                    it.onComplete()
                } else {
                    it.onError(
                        ResponseException(response.failed(), response.code())
                    )
                }
            }
        } catch (e: java.lang.Exception) {
            if (!it.isDisposed) {
                it.onError(e)
            }
        }
    }.onTerminateDetach().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
}


/**
 * 下载文件
 *
 * @param path String 网络路径, 非绝对路径会加上HOST为前缀
 * @see NetConfig.host
 * @param directory String 下载文件存放目录 {默认存在android/data/packageName/cache目录}
 * @param tag 可以传递对象给Request请求
 * @param isAbsolutePath Boolean 下载链接是否是绝对路径
 * @param block 请求参数
 * @return Observable<String> 结果会在主线程
 */
fun download(
    path: String,
    directory: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    isAbsolutePath: Boolean = false,
    block: (UrlDownload.Api.() -> Unit)? = null
): Observable<String> {

    return Observable.create<String> {
        try {
            val realPath = if (isAbsolutePath) path else (NetConfig.host + path)

            val download = Kalle.Download.get(realPath).directory(directory).tag(tag)

            val filePath = if (block == null) {
                download.perform()
            } else {
                download.apply(block).perform()
            }

            if (!it.isDisposed) {
                it.onNext(filePath)
                it.onComplete()
            }
        } catch (e: Exception) {
            if (!it.isDisposed) {
                it.onError(e)
            }
        }
    }.onTerminateDetach().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
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
fun Context.downloadImg(url: String, with: Int = -1, height: Int = -1): Observable<File> {

    return Observable.create<File> {

        Glide.with(this).downloadOnly()

        val download = Glide.with(this).download(url)

        val futureTarget = if (with == -1 && height == -1) {
            download.submit()
        } else {
            download.submit(with, height)
        }

        try {
            val file = futureTarget.get()
            if (!it.isDisposed) {
                it.onNext(file)
                it.onComplete()
            }
        } catch (e: Exception) {
            if (!it.isDisposed) it.onError(e)
        }

    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}

// </editor-fold>

// <editor-fold desc="同步请求">

inline fun <reified M> syncGet(
    path: String,
    tag: Any? = null,
    isAbsolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): Observable<M> {

    return Observable.create<M> {
        try {
            val get = Kalle.get(if (isAbsolutePath) path else (NetConfig.host + path)).tag(tag)
            val response = if (block == null) {
                get.perform(M::class.java, String::class.java)
            } else {
                get.apply(block).perform<M, String>(M::class.java, String::class.java)
            }

            if (!it.isDisposed) {
                if (response.isSucceed) {
                    it.onNext(response.succeed())
                    it.onComplete()
                } else {
                    it.onError(ResponseException(response.failed(), response.code()))
                }
            }
        } catch (e: java.lang.Exception) {
            if (!it.isDisposed) {
                it.onError(e)
            }
        }
    }.onTerminateDetach()
}

inline fun <reified M> syncPost(
    path: String,
    tag: Any? = null,
    isAbsolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): Observable<M> {

    return Observable.create<M> {
        try {
            val post = Kalle.post(if (isAbsolutePath) path else (NetConfig.host + path)).tag(tag)
            val response = if (block == null) {
                post.perform<M, String>(M::class.java, String::class.java)
            } else {
                post.apply(block).perform<M, String>(M::class.java, String::class.java)
            }

            if (!it.isDisposed) {
                if (response.isSucceed) {
                    it.onNext(response.succeed())
                    it.onComplete()
                } else {
                    it.onError(
                        ResponseException(response.failed(), response.code())
                    )
                }
            }
        } catch (e: java.lang.Exception) {
            if (!it.isDisposed) {
                it.onError(e)
            }
        }
    }.onTerminateDetach()
}

fun syncDownload(
    path: String,
    directory: String = NetConfig.app.externalCacheDir!!.absolutePath,
    tag: Any? = null,
    isAbsolutePath: Boolean = false,
    block: (UrlDownload.Api.() -> Unit)? = null
): Observable<String> {

    return Observable.create<String> {
        try {
            val realPath = if (isAbsolutePath) path else (NetConfig.host + path)

            val download = Kalle.Download.get(realPath).directory(directory).tag(tag)

            val filePath = if (block == null) {
                download.perform()
            } else {
                download.apply(block).perform()
            }

            if (!it.isDisposed) {
                it.onNext(filePath)
                it.onComplete()
            }
        } catch (e: Exception) {
            if (!it.isDisposed) {
                it.onError(e)
            }
        }
    }.onTerminateDetach()
}

fun Context.syncDownloadImg(url: String, with: Int = 0, height: Int = 0): Observable<File> {

    return Observable.create<File> {

        Glide.with(this).downloadOnly()

        val download = Glide.with(this).download(url)

        val futureTarget = if (with == 0 && height == 0) {
            download.submit()
        } else {
            download.submit(with, height)
        }

        try {
            val file = futureTarget.get()
            if (!it.isDisposed) {
                it.onNext(file)
                it.onComplete()
            }
        } catch (e: Exception) {
            if (!it.isDisposed) {
                it.onError(e)
            }
        }

    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}

// </editor-fold>


