/*
 * MIT License
 *
 * Copyright (c) 2023 劉強東 https://github.com/liangjingkanji
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.drake.net.time

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import java.io.Closeable
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * 创建一个轮询器
 *
 * 操作
 * 1. 开启 [start] 只有在闲置状态下才可以开始
 * 2. 停止 [stop]
 * 3. 暂停 [pause]
 * 4. 继续 [resume]
 * 5. 重置 [reset] 重置不会导致轮询器停止
 * 6. 开关 [switch] 开启|暂停切换
 * 7. 生命周期 [life]
 *
 * 函数回调: 允许多次订阅同一个轮询器
 * 1. 每个事件 [subscribe]
 * 2. 停止或者结束 [finish]
 *
 * @param end 结束值, -1 表示永远不结束
 * @param period 计时器间隔
 * @param unit 计时器单位
 * @param initialDelay 第一次事件的间隔时间, 默认0
 * @param start 开始值, 当[start]]比[end]值大, 且end不等于-1时, 即为倒计时, 反之正计时
 */
open class Interval @JvmOverloads constructor(
    var end: Long,
    private val period: Long,
    private val unit: TimeUnit,
    private val start: Long = 0,
    private val initialDelay: Long = 0
) : Serializable, Closeable {

    /**
     * 创建一个不会自动结束的轮询器/计时器
     *
     * @param period 间隔时间
     * @param unit 时间单位
     * @param initialDelay 初次间隔时间, 默认为0即立即开始
     */
    @JvmOverloads
    constructor(
        period: Long,
        unit: TimeUnit,
        initialDelay: Long = 0
    ) : this(-1, period, unit, 0, initialDelay)

    private val subscribeList: MutableList<Interval.(Long) -> Unit> = mutableListOf()
    private val finishList: MutableList<Interval.(Long) -> Unit> = mutableListOf()
    private var countTime = 0L
    private var delay = 0L
    private var scope: CoroutineScope? = null
    private lateinit var ticker: ReceiveChannel<Unit>

    /** 轮询器的计数 */
    var count = start

    /** 轮询器当前状态 */
    var state = IntervalStatus.STATE_IDLE
        private set

    // <editor-fold desc="回调">

    /**
     * 订阅轮询器
     * 每次轮询器计时都会调用该回调函数
     * 轮询器完成时会同时触发回调[block]和[finish]
     */
    fun subscribe(block: Interval.(Long) -> Unit) = apply {
        subscribeList.add(block)
    }

    /**
     * 轮询器完成时回调该函数
     * @see stop 执行该函数也会回调finish
     * @see cancel 使用该函数取消轮询器不会回调finish
     */
    fun finish(block: Interval.(Long) -> Unit) = apply {
        finishList.add(block)
    }

    // </editor-fold>

    // <editor-fold desc="操作">

    /**
     * 开始
     * 如果当前为暂停状态将重新开始轮询
     */
    fun start() = apply {
        if (state == IntervalStatus.STATE_ACTIVE) return this
        state = IntervalStatus.STATE_ACTIVE
        count = start
        launch()
    }

    /**
     * 停止
     */
    fun stop() {
        if (state == IntervalStatus.STATE_IDLE) return
        scope?.cancel()
        state = IntervalStatus.STATE_IDLE
        finishList.forEach {
            it.invoke(this, count)
        }
    }

    /**
     * 取消
     * 区别于[stop]并不会执行[finish]
     */
    fun cancel() {
        if (state == IntervalStatus.STATE_IDLE) return
        scope?.cancel()
        state = IntervalStatus.STATE_IDLE
    }

    /** 等于[cancel] */
    override fun close() = cancel()

    /**
     * 切换轮询器开始或结束
     * 假设轮询器为暂停[IntervalStatus.STATE_PAUSE]状态将继续运行[resume]
     */
    fun switch() {
        when (state) {
            IntervalStatus.STATE_ACTIVE -> stop()
            IntervalStatus.STATE_IDLE -> start()
            IntervalStatus.STATE_PAUSE -> resume()
        }
    }

    /**
     * 暂停
     */
    fun pause() {
        if (state != IntervalStatus.STATE_ACTIVE) return
        scope?.cancel()
        state = IntervalStatus.STATE_PAUSE
        // 一个计时单位的总时间减去距离上次计时已过的时间，等于resume时需要delay的时间
        delay = max(unit.toMillis(period) - (SystemClock.elapsedRealtime() - countTime), 0L)
    }

    /**
     * 继续
     * 要求轮询器为暂停状态[IntervalStatus.STATE_PAUSE], 否则无效
     */
    fun resume() {
        if (state != IntervalStatus.STATE_PAUSE) return
        state = IntervalStatus.STATE_ACTIVE
        launch(delay)
    }

    /**
     * 重置
     */
    fun reset() {
        count = start
        delay = unit.toMillis(initialDelay)
        scope?.cancel()
        if (state == IntervalStatus.STATE_ACTIVE) launch()
    }

    // </editor-fold>

    //<editor-fold desc="生命周期">
    /**
     * 自动在指定生命周期后取消[cancel]轮询器
     * @param lifecycleOwner 生命周期持有者, 一般为Activity/Fragment
     * @param lifeEvent 销毁生命周期, 默认为 [Lifecycle.Event.ON_DESTROY] 时停止时停止轮询器
     */
    @JvmOverloads
    fun life(
        lifecycleOwner: LifecycleOwner,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) = apply {
        runMain {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (lifeEvent == event) cancel()
                }
            })
        }
    }

    /**
     * 自动在指定生命周期后取消[cancel]轮询器
     * @param lifeEvent 销毁生命周期, 默认为 [Lifecycle.Event.ON_DESTROY] 时停止时停止轮询器
     */
    @JvmOverloads
    fun life(
        fragment: Fragment,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ): Interval {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) {
            it?.lifecycle?.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (lifeEvent == event) cancel()
                }
            })
        }
        return this
    }

    /**
     *  当界面不可见时暂停[pause], 当界面可见时继续[resume], 当界面销毁时[cancel]轮询器
     */
    fun onlyResumed(lifecycleOwner: LifecycleOwner) = apply {
        runMain {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> resume()
                        Lifecycle.Event.ON_PAUSE -> pause()
                        Lifecycle.Event.ON_DESTROY -> cancel()
                        else -> {}
                    }
                }
            })
        }
    }
    //</editor-fold>

    /** 启动轮询器 */
    @OptIn(ObsoleteCoroutinesApi::class)
    private fun launch(delay: Long = unit.toMillis(initialDelay)) {
        scope = CoroutineScope(Dispatchers.Main)
        scope?.launch {
            ticker = ticker(unit.toMillis(period), delay, mode = TickerMode.FIXED_DELAY)
            for (unit in ticker) {
                subscribeList.forEach {
                    it.invoke(this@Interval, count)
                }
                if (end != -1L && count == end) {
                    scope?.cancel()
                    state = IntervalStatus.STATE_IDLE
                    finishList.forEach {
                        it.invoke(this@Interval, count)
                    }
                }
                if (end != -1L && start > end) count-- else count++
                countTime = SystemClock.elapsedRealtime()
            }
        }
    }

    private fun runMain(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block()
        } else {
            Handler(Looper.getMainLooper()).post { block() }
        }
    }
}

