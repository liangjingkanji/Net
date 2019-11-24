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
import io.reactivex.observers.DisposableObserver

/**
 * 自动结束下拉刷新和上拉加载状态
 * 自动展示缺省页
 * 自动分页加载
 */
abstract class PageObserver<M>(val page: PageRefreshLayout) : DisposableObserver<M>() {


    val index
        get() = page.index

    private var error: (PageObserver<M>.(e: Throwable) -> Unit)? = null

    init {
        page.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                dispose()
            }
        })
    }

    abstract override fun onNext(it: M)

    /**
     * 关闭进度对话框并提醒错误信息
     */
    override fun onError(e: Throwable) {
        if (page.state == RefreshState.Refreshing) {
            page.showError()
        } else page.finish(false)
        error?.invoke(this, e) ?: handleError(e)
    }

    fun handleError(e: Throwable) {
        onStateError.invoke(e, page)
    }

    fun error(block: (PageObserver<M>.(e: Throwable) -> Unit)?): PageObserver<M> {
        error = block
        return this
    }

    /**
     * 自动判断是添加数据还是覆盖数据
     */
    fun addData(data: List<Any>, hasMore: BindingAdapter.() -> Boolean) {
        page.addData(data, hasMore)
    }


    /**
     * 显示空缺省页
     * 此操作会导致观察者取消订阅
     */
    fun showEmpty() {
        page.showEmpty()
    }

    override fun onComplete() {
    }

    /**
     * 显示内容缺省页
     */
    fun showContent() {
        page.showContent()
    }

    /**
     * 结束刷新或者加载
     * @param success 是否成功
     */
    fun finish(success: Boolean = true) {
        page.finish(success)
    }
}
