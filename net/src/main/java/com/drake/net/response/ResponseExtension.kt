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

package com.drake.net.response

import com.drake.net.component.Progress
import com.drake.net.exception.ConvertException
import com.drake.net.exception.DownloadFileException
import com.drake.net.exception.NetException
import com.drake.net.reflect.TypeToken
import com.drake.net.reflect.typeTokenOf
import com.drake.net.request.*
import com.drake.net.tag.NetTag
import com.drake.net.utils.md5
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketException
import java.net.URLDecoder
import kotlin.coroutines.cancellation.CancellationException

/**
 * 按照以下顺序返回最终的下载文件的名称
 *
 * 1. 指定文件名
 * 2. 响应头文件名
 * 3. 请求URL路径
 * 4. 时间戳
 */
fun Response.fileName(): String {
    request.downloadFileName().takeUnless { it.isNullOrBlank() }?.let { return it }
    val disposition = header("Content-Disposition")
    if (disposition != null) {
        disposition.substringAfter("filename=", "").takeUnless { it.isBlank() }?.let { return it }
        disposition.substringAfter("filename*=", "").trimStart(*"UTF-8''".toCharArray())
            .takeUnless { it.isBlank() }?.let { return it }
    }

    var fileName: String = request.url.pathSegments.last().substringBefore("?")
    fileName = if (fileName.isBlank()) "unknown_${System.currentTimeMillis()}" else {
        if (request.downloadFileNameDecode()) {
            try {
                URLDecoder.decode(fileName, "UTF8")
            } catch (e: Exception) {
                fileName
            }
        } else fileName
    }
    return fileName
}

/**
 * 下载到指定文件
 */
@Throws(DownloadFileException::class)
fun Response.file(): File? {
    var dir = request.downloadFileDir() // 下载目录
    val fileName: String // 下载文件名
    val dirFile = File(dir)
    // 判断downloadDir是否为目录
    var file = if (dirFile.isDirectory) {
        fileName = fileName()
        File(dir, fileName)
    } else {
        val temp = dir
        dir = dir.substringBeforeLast(File.separatorChar)
        fileName = temp.substringAfterLast(File.separatorChar)
        dirFile
    }
    try {
        if (file.exists()) {
            // MD5校验匹配文件
            if (request.downloadMd5Verify()) {
                val md5Header = header("Content-MD5")
                if (md5Header != null && file.md5(true) == md5Header) {
                    val downloadListeners = request.tagOf<NetTag.DownloadListeners>()
                    if (!downloadListeners.isNullOrEmpty()) {
                        val fileSize = file.length()
                        val progress = Progress().apply {
                            currentByteCount = fileSize
                            totalByteCount = fileSize
                            intervalByteCount = fileSize
                            finish = true
                        }
                        downloadListeners.forEach {
                            it.onProgress(progress)
                        }
                    }
                    return file
                }
            }
            // 命名冲突添加序列数字的后缀
            if (request.downloadConflictRename() && file.name == fileName) {
                val fileExtension = file.extension
                val fileNameWithoutExtension = file.nameWithoutExtension
                fun rename(index: Long): File {
                    file = File(dir, fileNameWithoutExtension + "_($index)" + fileExtension)
                    return if (file.exists()) {
                        rename(index + 1)
                    } else file
                }
                file = rename(1)
            }
        }

        // 临时文件
        if (request.downloadTempFile()) {
            file = File(dir, file.name + ".net-download")
        }
        val source = body?.source() ?: return null
        if (!file.exists()) file.createNewFile()
        file.sink().buffer().use {
            it.writeAll(source)
            source.closeQuietly()
        }
        // 下载完毕删除临时文件
        if (request.downloadTempFile()) {
            val fileFinal = File(dir, fileName)
            file.renameTo(fileFinal)
            return fileFinal
        }
        return file
    } catch (e: SocketException) {
        // 取消请求需要删除下载临时文件
        if (request.downloadTempFile()) file.delete()
        throw CancellationException(e)
    } catch (e: Exception) {
        throw DownloadFileException(this, cause = e)
    }
}

/**
 * 响应体使用转换器处理数据
 */
@Suppress("UNCHECKED_CAST")
@Throws(IOException::class)
inline fun <reified R> Response.convert(): R {
    try {
        return request.converter().onConvert<R>(typeTokenOf<R>(), this) as R
    } catch (e: NetException) {
        throw e
    } catch (e: Throwable) {
        throw ConvertException(this, cause = e)
    }
}

/**
 * 响应体使用转换器处理数据
 * 本方法仅为兼容Java使用存在
 * @param type 如果存在泛型嵌套要求使用[typeTokenOf]或者[TypeToken]获取, 否则泛型会被擦除导致无法解析
 */
@Suppress("UNCHECKED_CAST")
@Throws(IOException::class)
fun <R> Response.convert(type: Type): R {
    try {
        return request.converter().onConvert<R>(type, this) as R
    } catch (e: NetException) {
        throw e
    } catch (e: Throwable) {
        throw ConvertException(this, cause = e)
    }
}