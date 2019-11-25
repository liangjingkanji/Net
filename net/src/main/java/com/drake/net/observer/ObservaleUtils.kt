/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：11/25/19 1:03 PM
 */

package com.drake.net.observer

import android.os.Handler
import android.os.Looper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// <editor-fold desc="线程">

fun <M> Observable<M>.async(): Observable<M> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <M> Observable<M>.computation(): Observable<M> {
    return subscribeOn(Schedulers.computation())
}

fun <M> Observable<M>.io(): Observable<M> {
    return subscribeOn(Schedulers.io())
}

fun <M> Observable<M>.single(): Observable<M> {
    return subscribeOn(Schedulers.single())
}

fun <M> Observable<M>.newThread(): Observable<M> {
    return subscribeOn(Schedulers.newThread())
}

fun <M> Observable<M>.main(): Observable<M> {
    return observeOn(AndroidSchedulers.mainThread())
}

// </editor-fold>

fun runMain(block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        Handler(Looper.getMainLooper()).post { block() }
    }
}