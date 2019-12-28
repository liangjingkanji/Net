/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/22/19 3:59 PM
 */

@file:UseExperimental(ObsoleteCoroutinesApi::class)

package com.drake.net.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.drake.net.scope.AndroidScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * 轮循器
 *
 * 操作
 * 1. 开启: 只有在闲置状态下才可以开始
 * 2. 停止
 * 3. 暂停
 * 4. 继续
 * 5. 重置: 重置不会导致轮循器停止
 * 6. 开关: 开启|暂停切换
 * 7. 生命周期
 *
 * 回调: 允许多次订阅同一个轮循器
 * 1. 每个事件
 * 2. 停止或者结束
 *
 * @param end 结束值
 * @param period 事件间隔
 * @param unit 事件单位
 * @param initialDelay 第一次事件的间隔时间
 * @param start 开始值
 */
class Interval(
    var end: Long, // -1 表示永远不结束, 可以修改
    private val period: Long,
    private val unit: TimeUnit,
    private val initialDelay: Long = period,
    private val start: Long = 0
) : Serializable {

    constructor(
        period: Long,
        unit: TimeUnit,
        initialDelay: Long = period
    ) : this(-1, period, unit, initialDelay, 0)

    private val receiveList: MutableList<(Long) -> Unit> = mutableListOf()
    private val finishList: MutableList<(Long) -> Unit> = mutableListOf()
    private var _state = TickerState.STATE_IDLE
    private var countTime = 0L
    private var delay = 0L
    private var scope: AndroidScope? = null
    private lateinit var ticker: ReceiveChannel<Unit>

    var count = start
    val state get() = _state

    // <editor-fold desc="回调">

    /**
     * 订阅轮循器
     */
    fun subscribe(block: (Long) -> Unit): Interval {
        receiveList.add(block)
        return this
    }

    /**
     * 轮循器完成
     */
    fun finish(block: (Long) -> Unit): Interval {
        finishList.add(block)
        return this
    }

    // </editor-fold>

    // <editor-fold desc="操作">

    /**
     * 开始
     */
    fun start() {
        if (_state == TickerState.STATE_ACTIVE || _state == TickerState.STATE_PAUSE) {
            return
        }
        _state = TickerState.STATE_ACTIVE
        launch()
    }


    /**
     * 停止
     */
    fun stop() {
        if (_state == TickerState.STATE_IDLE) return
        _state = TickerState.STATE_IDLE
        scope?.cancel()
        finishList.forEach {
            it.invoke(count)
        }
        count = start
    }

    /**
     * 开关
     */
    fun switch() {
        when (state) {
            TickerState.STATE_ACTIVE -> stop()
            TickerState.STATE_IDLE -> start()
            else -> return
        }
    }

    /**
     * 继续
     */
    fun resume() {
        if (_state != TickerState.STATE_PAUSE) return
        _state = TickerState.STATE_ACTIVE
        launch(delay)
    }

    /**
     * 暂停
     */
    fun pause() {
        if (_state != TickerState.STATE_ACTIVE) return
        _state = TickerState.STATE_PAUSE
        delay = System.currentTimeMillis() - countTime
        scope?.cancel()
    }

    /**
     * 重置
     */
    fun reset() {
        if (_state == TickerState.STATE_IDLE) return
        count = start
        scope?.cancel()
        delay = unit.toMillis(initialDelay)
        if (_state == TickerState.STATE_ACTIVE) launch()
    }

    /**
     * 生命周期
     * @param lifecycleOwner 默认在销毁时取消轮循器
     */
    fun life(
        lifecycleOwner: LifecycleOwner,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ): Interval {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) scope?.cancel()
            }
        })
        return this
    }

    // </editor-fold>

    private fun launch(delay: Long = unit.toMillis(initialDelay)) {
        scope = scope {

            ticker = ticker(unit.toMillis(period), delay, mode = TickerMode.FIXED_DELAY)

            for (unit in ticker) {

                count++
                countTime = System.currentTimeMillis()

                receiveList.forEach {
                    it.invoke(count)
                }

                if (end != -1L && count == end) {
                    scope?.cancel()
                    finishList.forEach {
                        it.invoke(count)
                    }
                }
            }
        }
    }

    /**
     * 计时器的状态
     */
    enum class TickerState {
        STATE_ACTIVE, STATE_IDLE, STATE_PAUSE
    }
}