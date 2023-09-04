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

package com.drake.net.request

import android.net.Uri
import com.drake.net.interfaces.ProgressListener
import com.drake.net.utils.fileName
import com.drake.net.utils.toRequestBody
import com.drake.net.utils.toRequestBody1
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okio.ByteString
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

open class BodyRequest : BaseRequest() {

    /**
     * 请求体
     */
    open var body: RequestBody? = null

    /**
     * multipart请求体
     * 主要存放文件/IO流
     */
    open var partBody = MultipartBody.Builder()

    /**
     * 表单请求体
     * 当你设置`partBody`后当前表单请求体中的所有参数都会被存放到partBody中
     */
    open var formBody = FormBody.Builder()

    /**
     * multipart请求体的媒体类型
     */
    open var mediaType: MediaType = MediaConst.FORM

    /**
     * 请求方法
     */
    override var method = Method.POST

    //<editor-fold desc="Param">
    override fun param(name: String, value: String?) {
        formBody.add(name, value ?: return)
    }

    override fun param(name: String, value: String?, encoded: Boolean) {
        value ?: return
        if (encoded) {
            formBody.addEncoded(name, value)
        } else {
            formBody.add(name, value)
        }
    }

    override fun param(name: String, value: Number?) {
        value ?: return
        formBody.add(name, value.toString())
    }

    override fun param(name: String, value: Boolean?) {
        value ?: return
        formBody.add(name, value.toString())
    }

    fun param(name: String, value: RequestBody?) {
        value ?: return
        // 指定fileName可以作为判断为文件类型依据
        partBody.addFormDataPart(name, "RequestBody", value)
    }

    fun param(name: String, value: ByteString?) {
        value ?: return
        partBody.addFormDataPart(name, null, value.toRequestBody())
    }

    fun param(
        name: String, value: ByteArray?, offset: Int = 0,
        byteCount: Int = 0
    ) {
        value ?: return
        partBody.addFormDataPart(
            name,
            null,
            value.toRequestBody1(offset = offset, byteCount = byteCount)
        )
    }

    fun param(
        name: String, value: Uri?, offset: Long = 0,
        byteCount: Long = 0
    ) {
        value ?: return
        partBody.addFormDataPart(
            name,
            value.fileName(),
            value.toRequestBody(startRange = offset, endRange = byteCount)
        )
    }

    fun param(
        name: String, value: File?, offset: Long = 0,
        byteCount: Long = 0
    ) {
        value ?: return
        partBody.addFormDataPart(
            name,
            value.name,
            value.toRequestBody(offset = offset, byteCount = byteCount)
        )
    }

    fun param(name: String, value: List<File?>?) {
        value?.forEach { file ->
            param(name, file)
        }
    }

    fun param(name: String, fileName: String?, value: File?) {
        partBody.addFormDataPart(name, fileName, value?.toRequestBody() ?: return)
    }

    fun param(body: MultipartBody.Part) {
        partBody.addPart(body)
    }

    //</editor-fold>

    //<editor-fold desc="JSON">

    /**
     * 添加Json为请求体
     */
    fun json(body: JSONObject?) {
        this.body = body?.toString()?.toRequestBody(MediaConst.JSON)
    }

    /**
     * 添加Json为请求体
     */
    fun json(body: JSONArray?) {
        this.body = body?.toString()?.toRequestBody(MediaConst.JSON)
    }

    /**
     * 添加Json为请求体
     */
    fun json(body: String?) {
        this.body = body?.toRequestBody(MediaConst.JSON)
    }

    /**
     * 添加Json为请求体
     */
    fun json(body: Map<String, Any?>?) {
        this.body = JSONObject(body ?: return).toString().toRequestBody(MediaConst.JSON)
    }

    /**
     * 添加Json对象为请求体
     */
    fun json(vararg body: Pair<String, Any?>) {
        this.body = JSONObject(body.toMap()).toString().toRequestBody(MediaConst.JSON)
    }
    //</editor-fold>

    /**
     * 添加上传进度监听器
     */
    fun addUploadListener(progressListener: ProgressListener) {
        okHttpRequest.uploadListeners().add(progressListener)
    }

    override fun buildRequest(): Request {
        val body = if (body != null) body else {
            val form = formBody.build()
            try {
                partBody.build()
                for (i in 0 until form.size) {
                    val name = form.name(i)
                    val value = form.value(i)
                    partBody.addFormDataPart(name, value)
                }
                partBody.setType(mediaType).build()
            } catch (e: IllegalStateException) {
                form
            }
        }

        return okHttpRequest.method(method.name, body)
            .url(httpUrl.build())
            .setConverter(converter)
            .build()
    }
}