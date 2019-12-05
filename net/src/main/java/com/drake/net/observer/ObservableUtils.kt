/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：11/25/19 1:03 PM
 */

package com.drake.net.observer

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber

// <editor-fold desc="被观察者线程">


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
    return subscribeOn(AndroidSchedulers.mainThread())
}


// </editor-fold>


// <editor-fold desc="观察者线程">

fun <M> Observable<M>.observeMain(): Observable<M> {
    return observeOn(AndroidSchedulers.mainThread())
}

fun <M> Observable<M>.observeSingle(): Observable<M> {
    return observeOn(Schedulers.single())
}

fun <M> Observable<M>.observeIO(): Observable<M> {
    return observeOn(Schedulers.io())
}

fun <M> Observable<M>.observeThread(): Observable<M> {
    return observeOn(Schedulers.newThread())
}

fun <M> Observable<M>.observeComputation(): Observable<M> {
    return observeOn(Schedulers.computation())
}

// </editor-fold>

/**
 * 在主线程运行
 */
fun runMain(block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        Handler(Looper.getMainLooper()).post { block() }
    }
}


/**
 * io线程 -> main线程
 */
fun <M> Observable<M>.async(): Observable<M> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

// <editor-fold desc="创建被观察者">

/**
 * 快速创建一个被观察者
 * @param M 返回值定义被观察者发射事件类型
 */
fun <M> from(block: () -> M): Observable<M> {
    return Observable.fromCallable(block)
}

fun <M> publish(block: (emitter: Subscriber<in M>) -> Unit): Observable<M> {
    return Observable.fromPublisher(block)
}

// </editor-fold>


/**
 * 自动跟随生命周期取消观察者订阅
 */
fun <T> Observable<T>.auto(
    lifecycleOwner: LifecycleOwner,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): Observable<T> {

    return doOnLifecycle({ dispose ->

        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {

            override fun onStateChanged(source: LifecycleOwner, actualEvent: Lifecycle.Event) {
                if (event == actualEvent) {
                    dispose.dispose()
                }
            }

        })
    }, {})
}