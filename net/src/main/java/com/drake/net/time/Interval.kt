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

@file:OptIn(ObsoleteCoroutinesApi::class)

package com.drake.net.time

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.drake.net.scope.AndroidScope
import com.drake.net.utils.scope
import kotlinx.coroutines.ObsoleteCoroutinesApi
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
 * @param period 计时器间隔
 * @param unit 计时器单位
 * @param initialDelay 第一次事件的间隔时间, 默认0
 * @param start 开始值, 当[start]]比[end]值大, 且end不等于-1时, 即为倒计时, 反之正计时
 *
 * @property count 轮循器的计数
 * @property state 轮循器当前状态
 */
class Interval(
    var end: Long, // -1 表示永远不结束
    private val period: Long,
    private val unit: TimeUnit,
    private val start: Long = 0,
    private val initialDelay: Long = 0
) : Serializable {

    constructor(
        period: Long,
        unit: TimeUnit,
        initialDelay: Long = 0
    ) : this(-1, period, unit, 0, initialDelay)

    private val listReceive: MutableList<(Long) -> Unit> = mutableListOf()
    private val listFinish: MutableList<(Long) -> Unit> = mutableListOf()
    private var countTime = 0L
    private var delay = 0L
    private var scope: AndroidScope? = null
    private lateinit var ticker: ReceiveChannel<Unit>

    var count = start
    var state = IntervalStatus.STATE_IDLE
        private set

    // <editor-fold desc="回调">

    /**
     * 订阅轮循器
     * 每次轮循器计时都会调用该回调函数
     * 轮循器完成时会同时触发回调[block]和[finish]
     */
    fun subscribe(block: (Long) -> Unit): Interval {
        listReceive.add(block)
        return this
    }

    /**
     * 轮循器完成时回调该函数
     */
    fun finish(block: (Long) -> Unit): Interval {
        listFinish.add(block)
        return this
    }

    // </editor-fold>

    // <editor-fold desc="操作">

    /**
     * 开始
     */
    fun start() {
        if (state == IntervalStatus.STATE_ACTIVE || state == IntervalStatus.STATE_PAUSE) {
            return
        }
        state = IntervalStatus.STATE_ACTIVE
        launch()
    }


    /**
     * 停止
     */
    fun stop() {
        if (state == IntervalStatus.STATE_IDLE) return
        state = IntervalStatus.STATE_IDLE
        scope?.cancel()
        listFinish.forEach {
            it.invoke(count)
        }
        count = start
    }

    /**
     * 切换轮循器开始或结束
     */
    fun switch() {
        when (state) {
            IntervalStatus.STATE_ACTIVE -> stop()
            IntervalStatus.STATE_IDLE -> start()
            else -> return
        }
    }

    /**
     * 继续
     * 要求轮循器为暂停状态[IntervalStatus.STATE_PAUSE], 否则无效
     */
    fun resume() {
        if (state != IntervalStatus.STATE_PAUSE) return
        state = IntervalStatus.STATE_ACTIVE
        launch(delay)
    }

    /**
     * 暂停
     */
    fun pause() {
        if (state != IntervalStatus.STATE_ACTIVE) return
        state = IntervalStatus.STATE_PAUSE
        delay = System.currentTimeMillis() - countTime
        scope?.cancel()
    }

    /**
     * 重置
     */
    fun reset() {
        if (state == IntervalStatus.STATE_IDLE) return
        count = start
        scope?.cancel()
        delay = unit.toMillis(initialDelay)
        if (state == IntervalStatus.STATE_ACTIVE) launch()
    }

    // </editor-fold>

    //<editor-fold desc="生命周期">
    /**
     * 绑定生命周期, 在指定生命周期发生时取消轮循器
     * @param lifecycleOwner 默认在销毁时取消轮循器
     * @param lifeEvent 销毁的时机, 默认为 ON_STOP 界面停止时停止轮循器
     *
     * Fragment的显示/隐藏不会调用onDestroy, 故轮循器默认是在ON_STOP停止, 如果你设置ON_DESTORY请考虑Fragment的情况下
     */
    fun life(
        lifecycleOwner: LifecycleOwner,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP
    ): Interval {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) scope?.cancel()
            }
        })
        return this
    }
    //</editor-fold>

    private fun launch(delay: Long = unit.toMillis(initialDelay)) {
        scope = scope {

            ticker = ticker(unit.toMillis(period), delay, mode = TickerMode.FIXED_DELAY)

            for (unit in ticker) {

                listReceive.forEach {
                    it.invoke(count)
                }

                if (end != -1L && count == end) {
                    scope?.cancel()
                    listFinish.forEach {
                        it.invoke(count)
                    }
                }

                if (end != -1L && start > end) count-- else count++
                countTime = System.currentTimeMillis()
            }
        }
    }
}

