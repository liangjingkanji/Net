/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 2:19 PM
 */

package com.drake.net.scope

import android.view.View
import com.drake.brv.BindingAdapter
import com.drake.brv.PageRefreshLayout
import com.drake.net.NetConfig
import com.scwang.smart.refresh.layout.constant.RefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
class PageCoroutineScope(
    val page: PageRefreshLayout,
    val block: suspend CoroutineScope.(PageCoroutineScope) -> Unit
) : AndroidScope() {

    val index get() = page.index

    init {
        page.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        if (page.state == RefreshState.Refreshing) {
            page.showError()
        } else page.finish(false)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null) {
            if (page.stateEnabled) page.showContent() else page.finish()
        }
    }

    override fun handleError(e: Throwable) {
        NetConfig.onStateError.invoke(e, page)
    }

    /**
     * 自动判断是添加数据还是覆盖数据, 以及数据为空或者NULL时[showEmpty]
     * @param hasMore 如果不传数据, 默认已加载完全部(建议此时可以关闭[PageRefreshLayout]的加载更多功能)
     */
    fun addData(
        data: List<Any>,
        bindingAdapter: BindingAdapter? = null,
        hasMore: BindingAdapter.() -> Boolean = { false }
    ) {
        page.addData(data, bindingAdapter, hasMore)
    }

    /**
     * 显示空缺省页
     * 此操作会导致观察者取消订阅
     */
    fun showEmpty() {
        page.showEmpty()
        cancel()
    }

    /**
     * 显示内容缺省页
     * 默认情况会自动执行不需要手动调用
     */
    fun showContent() {
        page.showContent()
        cancel()
    }

    /**
     * 结束刷新或者加载
     * 默认情况会自动执行不需要手动调用
     * @param success 是否成功
     */
    fun finish(success: Boolean = true) {
        page.finish(success)
        cancel()
    }

}