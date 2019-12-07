/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/7/19 1:08 PM
 */
package com.drake.net.observable

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.internal.schedulers.TrampolineScheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference


/**
 * 非常强大的轮循器
 *
 * 多个观察者可观察同一个计时器
 *
 * 支持 开始 | 暂停 | 继续 | 结束 | 重置 | 中途修改计数器
 */
class Interval(
    var end: Long, // -1 表示永远不结束, 可以修改
    private val period: Long,
    private val unit: TimeUnit,
    private val initialDelay: Long = 0,
    private val start: Long = 0,
    private val scheduler: Scheduler = Schedulers.computation()
) : Observable<Long>() {

    /**
     * 不会自动结束的轮循器
     */
    constructor(
        period: Long,
        unit: TimeUnit,
        initialDelay: Long = 0,
        scheduler: Scheduler = Schedulers.computation()
    ) : this(-1, period, unit, initialDelay, 0, scheduler)

    private var observerList = ArrayList<IntervalRangeObserver>()
    private var pause = false
    private var stop = false
    private var dispose: Disposable? = null
    private val iterator = {

        if (!pause) {
            synchronized(this) {
                count += 1
            }
            for (i in 0 until observerList.size) {
                observerList[i].run(count, end, stop)
            }
        }
    }

    public override fun subscribeActual(observer: Observer<in Long?>) {

        val agentObserver = IntervalRangeObserver(observer)
        observerList.add(agentObserver)

        observer.onSubscribe(agentObserver)

        if (dispose == null) init()
        agentObserver.setResource(dispose)
    }

    private fun init() {
        if (observerList.isEmpty()) return

        dispose = if (scheduler is TrampolineScheduler) {
            val worker = scheduler.createWorker()
            worker.schedulePeriodically(iterator, initialDelay, period, unit)
            worker
        } else {
            scheduler.schedulePeriodicallyDirect(iterator, initialDelay, period, unit)
        }
    }

    // <editor-fold desc="操作">


    /**
     * 计数器
     */
    var count = start

    /**
     * 停止轮循器
     * 如果开启轮循器请订阅观察者[subscribe], 多个观察者观察的同一轮循计数器
     */
    fun stop() {
        stop = true
    }

    /**
     * 重置轮循器
     * count = start , 计时器重置
     */
    fun reset() {
        count = start
        dispose?.dispose()
        init()
    }

    /**
     * 暂停轮循
     */
    fun pause() {
        pause = true
    }

    /**
     * 如果轮循器被暂停的情况下继续轮循器
     */
    fun resume() {
        pause = false
    }

    // </editor-fold>

    private class IntervalRangeObserver(
        private val downstream: Observer<in Long?>
    ) : AtomicReference<Disposable?>(), Disposable {

        override fun dispose() {
            DisposableHelper.dispose(this)
        }

        override fun isDisposed(): Boolean {
            return get() === DisposableHelper.DISPOSED
        }

        fun run(count: Long, end: Long, stop: Boolean) {
            if (!isDisposed) {

                downstream.onNext(count)

                if (end != -1L && count == end || stop) {
                    DisposableHelper.dispose(this)
                    downstream.onComplete()
                    return
                }
            }
        }

        fun setResource(d: Disposable?) {
            DisposableHelper.setOnce(this, d)
        }

        companion object {
            private const val serialVersionUID = 1891866368734007884L
        }

    }

}