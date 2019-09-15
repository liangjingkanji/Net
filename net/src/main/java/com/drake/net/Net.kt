/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net

import com.drake.net.exception.ResponseException
import com.yanzhenjie.kalle.Kalle
import com.yanzhenjie.kalle.download.UrlDownload
import com.yanzhenjie.kalle.simple.SimpleBodyRequest
import com.yanzhenjie.kalle.simple.SimpleUrlRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Get请求
 * @param path String 默认加上Host
 * @param isAbsolutePath Boolean Path是否是绝对路径
 * @param block SimpleUrlRequest.Api.() -> UnitUtils
 * @return Observable<M>
 */
inline fun <reified M> get(
    path: String,
    isAbsolutePath: Boolean = false,
    noinline block: (SimpleUrlRequest.Api.() -> Unit)? = null
): Observable<M> {

    return Observable.create<M> {
        try {
            val get = Kalle.get(if (isAbsolutePath) path else (NetConfig.host + path))
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
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * Post提交
 * @param path String 默认加上Host
 * @param isAbsolutePath Boolean 是否是绝对路径
 * @param block SimpleBodyRequest.Api.() -> UnitUtils
 * @return Observable<M>
 */
inline fun <reified M> post(
    path: String,
    isAbsolutePath: Boolean = false,
    noinline block: (SimpleBodyRequest.Api.() -> Unit)? = null
): Observable<M> {

    return Observable.create<M> {
        try {
            val post = Kalle.post(if (isAbsolutePath) path else (NetConfig.host + path))
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
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}


/**
 * 下载文件
 *
 * @param path String 自动加上Host
 * @param directory String 下载文件存放目录 {默认存在android/data/packageName/cache目录}
 * @param isAbsolutePath Boolean 下载链接是否是绝对路径
 * @return Observable<String>
 */
fun download(
    path: String,
    directory: String = NetConfig.App.externalCacheDir!!.absolutePath,
    isAbsolutePath: Boolean = false,
    block: (UrlDownload.Api.() -> Unit)? = null
): Observable<String> {

    return Observable.create<String> {
        try {
            val download = Kalle.Download.get(if (isAbsolutePath) path else (NetConfig.host + path))
                .directory(directory)

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
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}


