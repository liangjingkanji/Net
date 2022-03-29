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

@file:Suppress("unused", "FunctionName") @file:JvmName("NetKt")

package com.drake.net

import android.util.Log
import com.drake.net.interfaces.ProgressListener
import com.drake.net.request.*
import com.drake.net.tag.NetTag
import okhttp3.Request
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

object Net {

    //<editor-fold desc="同步执行网络请求">
    /**
     * 同步网络请求
     *
     * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
     * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
     * @param block 函数中可以配置请求参数
     */
    fun get(
        path: String,
        tag: Any? = null,
        block: (UrlRequest.() -> Unit)? = null
    ) = UrlRequest().apply {
        setPath(path)
        method = Method.GET
        tag(tag)
        block?.invoke(this)
    }

    /**
     * 同步网络请求
     *
     * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
     * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
     * @param block 函数中可以配置请求参数
     */
    fun post(
        path: String,
        tag: Any? = null,
        block: (BodyRequest.() -> Unit)? = null
    ) = BodyRequest().apply {
        setPath(path)
        method = Method.POST
        tag(tag)
        block?.invoke(this)
    }

    /**
     * 同步网络请求
     *
     * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
     * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
     * @param block 函数中可以配置请求参数
     */
    fun head(
        path: String,
        tag: Any? = null,
        block: (UrlRequest.() -> Unit)? = null
    ) = UrlRequest().apply {
        setPath(path)
        method = Method.HEAD
        tag(tag)
        block?.invoke(this)
    }

    /**
     * 同步网络请求
     *
     * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
     * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
     * @param block 函数中可以配置请求参数
     */
    fun options(
        path: String,
        tag: Any? = null,
        block: (UrlRequest.() -> Unit)? = null
    ) = UrlRequest().apply {
        setPath(path)
        method = Method.OPTIONS
        tag(tag)
        block?.invoke(this)
    }

    /**
     * 同步网络请求
     *
     * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
     * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
     * @param block 函数中可以配置请求参数
     */
    fun trace(
        path: String,
        tag: Any? = null,
        block: (UrlRequest.() -> Unit)? = null
    ) = UrlRequest().apply {
        setPath(path)
        method = Method.TRACE
        tag(tag)
        block?.invoke(this)
    }

    /**
     * 同步网络请求
     *
     * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
     * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
     * @param block 函数中可以配置请求参数
     */
    fun delete(
        path: String,
        tag: Any? = null,
        block: (BodyRequest.() -> Unit)? = null
    ) = BodyRequest().apply {
        setPath(path)
        method = Method.DELETE
        tag(tag)
        block?.invoke(this)
    }

    /**
     * 同步网络请求
     *
     * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
     * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
     * @param block 函数中可以配置请求参数
     */
    fun put(
        path: String,
        tag: Any? = null,
        block: (BodyRequest.() -> Unit)? = null
    ) = BodyRequest().apply {
        setPath(path)
        method = Method.PUT
        tag(tag)
        block?.invoke(this)
    }

    /**
     * 同步网络请求
     *
     * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
     * @param tag 可以传递对象给Request请求, 一般用于在拦截器/转换器中进行针对某个接口行为判断
     * @param block 函数中可以配置请求参数
     */
    fun patch(
        path: String,
        tag: Any? = null,
        block: (BodyRequest.() -> Unit)? = null
    ) = BodyRequest().apply {
        setPath(path)
        method = Method.PATCH
        tag(tag)
        block?.invoke(this)
    }
    //</editor-fold>

    //<editor-fold desc="取消网络请求">
    /**
     * 取消全部网络请求
     */
    fun cancelAll() {
        NetConfig.runningCalls.forEach { it.get()?.cancel() }
        NetConfig.runningCalls.clear()
        NetConfig.okHttpClient.dispatcher.cancelAll()
    }

    /**
     * 取消指定的网络请求, Id理论上是唯一的, 所以该函数一次只能取消一个请求
     * @return 如果成功取消返回true
     */
    fun cancelId(id: Any?): Boolean {
        if (id == null) return false
        val iterator = NetConfig.runningCalls.iterator()
        while (iterator.hasNext()) {
            val call = iterator.next().get() ?: continue
            if (id == call.request().tagOf<NetTag.RequestId>()?.value) {
                call.cancel()
                iterator.remove()
                return true
            }
        }
        return false
    }

    /**
     * 根据分组取消网络请求
     * @return 如果成功取消返回true, 无论取消个数
     */
    fun cancelGroup(group: Any?): Boolean {
        if (group == null) return false
        val iterator = NetConfig.runningCalls.iterator()
        var hasCancel = false
        while (iterator.hasNext()) {
            val call = iterator.next().get() ?: continue
            val value = call.request().tagOf<NetTag.RequestGroup>()?.value
            if (group == value) {
                call.cancel()
                iterator.remove()
                hasCancel = true
            }
        }
        return hasCancel
    }
    //</editor-fold>

    //<editor-fold desc="监听请求进度">
    /**
     * 监听正在请求的上传进度
     * @param id 请求的Id
     * @see com.drake.net.request.BaseRequest.setId
     */
    fun addUploadListener(id: Any, progressListener: ProgressListener) {
        NetConfig.runningCalls.forEach {
            val request = it.get()?.request() ?: return@forEach
            if (request.id == id) {
                request.uploadListeners().add(progressListener)
            }
        }
    }

    /**
     * 删除正在请求的上传进度监听器
     * @param id 请求的Id
     * @see com.drake.net.request.BaseRequest.setId
     */
    fun removeUploadListener(id: Any, progressListener: ProgressListener) {
        NetConfig.runningCalls.forEach {
            val request = it.get()?.request() ?: return@forEach
            if (request.id == id) {
                request.uploadListeners()?.remove(progressListener)
            }
        }
    }

    /**
     * 监听正在请求的下载进度
     * @param id 请求的Id
     * @see com.drake.net.request.BaseRequest.setId
     */
    fun addDownloadListener(id: Any, progressListener: ProgressListener) {
        NetConfig.runningCalls.forEach {
            val request = it.get()?.request() ?: return@forEach
            if (request.id == id) {
                request.downloadListeners().add(progressListener)
            }
        }
    }

    /**
     * 删除正在请求的下载进度监听器
     *
     * @param id 请求的Id
     * @see com.drake.net.request.BaseRequest.setId
     */
    fun removeDownloadListener(id: Any, progressListener: ProgressListener) {
        NetConfig.runningCalls.forEach {
            val request = it.get()?.request() ?: return@forEach
            if (request.id == id) {
                request.downloadListeners()?.remove(progressListener)
            }
        }
    }

    //</editor-fold>

    //<editor-fold desc="Request">
    /**
     * 指定Id的请求
     */
    fun requestById(id: Any): Request? {
        NetConfig.runningCalls.forEach {
            val request = it.get()?.request() ?: return@forEach
            if (request.id == id) return request
        }
        return null
    }

    /**
     * 指定分组的请求
     */
    fun requestByGroup(group: Any): ArrayList<Request> {
        val requests = ArrayList<Request>()
        NetConfig.runningCalls.forEach {
            val request = it?.get()?.request() ?: return@forEach
            if (request.group == group) requests.add(request)
        }
        return requests
    }
    //</editor-fold>

    //<editor-fold desc="日志">
    /**
     * 输出异常日志
     * @see NetConfig.logTag
     */
    fun printStackTrace(t: Throwable) {
        if (NetConfig.logEnabled) {
            Log.d(NetConfig.logTag, getStackTraceString(t))
        }
    }

    /**
     * 返回异常堆栈日志字符串
     * 如果tr为null则返回空字符串
     */
    private fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return t.message ?: ""
            }
            t = t.cause
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
    //</editor-fold>
}
