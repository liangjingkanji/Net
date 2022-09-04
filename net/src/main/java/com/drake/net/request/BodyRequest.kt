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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.drake.net.request

import android.net.Uri
import com.drake.net.interfaces.ProgressListener
import com.drake.net.utils.fileName
import com.drake.net.utils.toRequestBody
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
        partBody.addFormDataPart(name, null, value)
    }

    fun param(name: String, value: ByteString?) {
        value ?: return
        partBody.addFormDataPart(name, null, value.toRequestBody())
    }

    fun param(name: String, value: ByteArray?) {
        value ?: return
        partBody.addFormDataPart(name, null, value.toRequestBody())
    }
    
    fun param(name: String, value: Uri?) {
        value ?: return
        partBody.addFormDataPart(name, value.fileName(), value.toRequestBody())
    }

    fun param(name: String, value: File?) {
        value ?: return
        partBody.addFormDataPart(name, value.name, value.toRequestBody())
    }

    fun param(name: String, value: List<File?>?) {
        value?.forEach { file ->
            param(name, file)
        }
    }

    fun param(name: String, fileName: String?, value: File?) {
        partBody.addFormDataPart(name, fileName, value?.toRequestBody() ?: return)
    }

    fun param(body: RequestBody, header: Headers? = null) {
        partBody.addPart(header, body)
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