/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：11/28/19 11:45 PM
 */
package com.drake.net

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
 * 轮循器
 *
 * 多个观察者可观察同一个计时器
 * 开始 | 暂停 | 继续 | 结束
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


    public override fun subscribeActual(observer: Observer<in Long?>) {

        val agentObserver =
            IntervalRangeObserver(observer)

        observerList.add(agentObserver)
        observer.onSubscribe(agentObserver)

        if (observerList.size == 1) {
            val sch = scheduler
            val iterator = {

                if (!pause) {
                    synchronized(this) {
                        count += 1
                    }
                    for (i in 0 until observerList.size) {
                        observerList[i].run(count, end, stop)
                    }
                }
            }
            if (sch is TrampolineScheduler) {
                val worker = sch.createWorker()
                agentObserver.setResource(worker)
                worker.schedulePeriodically(iterator, initialDelay, period, unit)
            } else {
                val d = sch.schedulePeriodicallyDirect(iterator, initialDelay, period, unit)
                agentObserver.setResource(d)
            }
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
     */
    fun reset() {
        count = start
    }

    /**
     * 暂停轮循
     */
    fun pause() {
        pause = true
    }

    /**
     * 继续轮循器
     */
    fun resume() {
        pause = false
    }

    // </editor-fold>

    class IntervalRangeObserver(
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