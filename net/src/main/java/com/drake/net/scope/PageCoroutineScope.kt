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
import com.drake.net.R
import kotlinx.coroutines.cancel

@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
class PageCoroutineScope(
    val page: PageRefreshLayout
) : NetCoroutineScope() {

    val index get() = page.index

    private var load
        get() = page.getTag(R.id.load) as? Boolean ?: false
        set(value) = page.setTag(R.id.load, value)

    init {
        page.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun start() {
        (page.getTag(R.id.cache_succeed) as? Boolean)?.let {
            readCache = false
            cacheSucceed = it
        }
    }

    override fun readCache(succeed: Boolean) {
        if (succeed) {
            page.showContent()
        }
        page.setTag(R.id.cache_succeed, succeed)
    }

    override fun catch(e: Throwable) {
        super.catch(e)

        if (cacheSucceed || load) {
            finish(false)
        } else {
            page.showError()
        }
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null && auto) {
            page.showContent()
            load = true
        }
    }

    override fun handleError(e: Throwable) {
        if (cacheSucceed || load) {
            NetConfig.onError(e)
        } else {
            NetConfig.onStateError(e, page)
        }
    }

    /**
     * 自动判断是添加数据还是覆盖数据, 以及数据为空或者NULL时[showEmpty]
     *
     * @param hasMore 如果不传数据, 默认已加载完全部 (建议此时可以关闭[PageRefreshLayout]的加载更多功能)
     */
    fun addData(
        data: List<Any>,
        bindingAdapter: BindingAdapter? = null,
        hasMore: BindingAdapter.() -> Boolean = { false }
    ) {
        page.addData(data, bindingAdapter, hasMore)
        autoOff()
    }

    /**
     * 显示空缺省页
     */
    fun showEmpty() {
        page.showEmpty()
        autoOff()
    }

    /**
     * 显示内容缺省页
     *
     * 一般情况会自动执行不需要手动调用
     */
    fun showContent() {
        page.showContent()
        load = true
        autoOff()
    }

    /**
     * 结束刷新或者加载
     *
     * 一般情况会自动执行不需要手动调用
     *
     * @param success 是否成功
     */
    fun finish(success: Boolean = true) {
        page.finish(success)
        autoOff()
    }

}