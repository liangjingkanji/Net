/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/7/19 7:08 PM
 */
package com.drake.net.observable

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Timed
import java.util.concurrent.TimeUnit

/**
 * 第一次事件的时间间隔[Timed.time]为 -1
 */
fun <T> Observable<T>.interval(
    unit: TimeUnit,
    scheduler: Scheduler = Schedulers.computation()
): TimeInterval<T> {
    return TimeInterval(this, unit, scheduler)
}

class TimeInterval<T>(
    private val source: ObservableSource<T>,
    private val unit: TimeUnit,
    private val scheduler: Scheduler
) : Observable<Timed<T>>() {

    override fun subscribeActual(observer: Observer<in Timed<T>>) {
        val timeIntervalObserver = TimeIntervalObserver(observer, unit, scheduler)
        source.subscribe(timeIntervalObserver)
    }

    internal class TimeIntervalObserver<T>(
        private val downstream: Observer<in Timed<T>>,
        private val unit: TimeUnit,
        private val scheduler: Scheduler
    ) : Observer<T>, Disposable {

        private var lastTime = 0L
        private var first = true
        private var upstream: Disposable? = null

        override fun onSubscribe(d: Disposable) {
            if (DisposableHelper.validate(upstream, d)) {
                upstream = d

                downstream.onSubscribe(this)
            }
        }

        override fun dispose() {
            upstream!!.dispose()
        }

        override fun isDisposed(): Boolean {
            return upstream!!.isDisposed
        }

        override fun onNext(t: T) {
            val now = scheduler.now(unit)

            val delta = if (first) {
                first = false; -1
            } else now - lastTime

            downstream.onNext(Timed(t, delta, unit))
            lastTime = now
        }

        override fun onError(t: Throwable) {
            downstream.onError(t)
        }

        override fun onComplete() {
            downstream.onComplete()
        }

    }

}