/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.observer

import android.view.View
import android.view.View.OnAttachStateChangeListener
import com.drake.brv.BindingAdapter
import com.drake.brv.PageRefreshLayout
import com.drake.net.NetConfig.onStateError
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 * 自动结束下拉刷新和上拉加载状态
 * 自动展示缺省页
 * 自动分页加载
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class PageObserver<M>(val page: PageRefreshLayout) :
    TryObserver<PageObserver<M>, M>() {


    val index get() = page.index

    init {
        page.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                dispose()
            }
        })
    }

    /**
     * 关闭进度对话框并提醒错误信息
     */
    override fun tryError(e: Throwable) {
        super.tryError(e)
        if (page.state == RefreshState.Refreshing) {
            page.showError()
        } else page.finish(false)
    }

    override fun handleError(e: Throwable) {
        onStateError.invoke(e, page)
    }

    /**
     * 自动判断是添加数据还是覆盖数据, 以及数据为空或者NULL时[showEmpty]
     * @param hasMore 如果不穿数据, 默认已加载完全部(建议此时可以关闭[PageRefreshLayout]的加载更多功能)
     */
    fun addData(data: List<Any>, hasMore: BindingAdapter.() -> Boolean = { false }) {
        page.addData(data, hasMore)
        dispose()
    }


    /**
     * 显示空缺省页
     * 此操作会导致观察者取消订阅
     */
    fun showEmpty() {
        page.showEmpty()
        dispose()
    }

    override fun tryComplete() {
        if (page.stateEnabled) page.showContent() else page.finish()
    }

    /**
     * 显示内容缺省页
     * 默认情况会自动执行不需要手动调用
     */
    fun showContent() {
        page.showContent()
        dispose()
    }

    /**
     * 结束刷新或者加载
     * 默认情况会自动执行不需要手动调用
     * @param success 是否成功
     */
    fun finish(success: Boolean = true) {
        page.finish(success)
        dispose()
    }
}
