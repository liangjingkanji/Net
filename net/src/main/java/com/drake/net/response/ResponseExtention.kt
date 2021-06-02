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

import com.drake.net.body.peekString
import com.drake.net.exception.DownloadFileException
import com.drake.net.request.*
import com.drake.net.utils.isValid
import com.drake.net.utils.md5
import okhttp3.Response
import okio.buffer
import okio.sink
import java.io.File
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
    request.downloadFileName().isValid { return it }
    val disposition = header("Content-Disposition")
    if (disposition != null) {
        disposition.substringAfter("filename=", "").isValid { return it }
        disposition.substringAfter("filename*=", "").trimStart(*"UTF-8''".toCharArray())
            .isValid { return it }
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
    val downloadDir = request.downloadFileDir()
    val fileName = fileName()
    var file = File(downloadDir, fileName)
    try {
        if (file.exists()) {
            // MD5校验匹配文件
            if (request.downloadMd5Verify()) {
                val md5Header = request.header("Content-MD5")
                if (file.md5() == md5Header) return file
            }
            // 命名冲突添加序列数字的后缀
            if (request.downloadConflictRename() && file.name == fileName) {
                val fileExtension = file.extension
                val fileNameWithoutExtension = file.nameWithoutExtension
                fun rename(index: Long): File {
                    file = File(downloadDir, fileNameWithoutExtension + "_($index)" + fileExtension)
                    return if (file.exists()) {
                        rename(index + 1)
                    } else file
                }
                return rename(1)
            }
            request.downloadFileNameDecode()
        }

        // 临时文件
        if (request.downloadTempFile()) {
            file = File(downloadDir, file.name + ".net-download")
        }
        val source = body?.source() ?: return null
        if (!file.exists()) file.createNewFile()
        file.sink().buffer().use {
            it.writeAll(source)
        }
        // 下载完毕删除临时文件
        if (request.downloadTempFile()) {
            file.renameTo(File(downloadDir, fileName))
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
 * 响应日志信息
 * 只会输出 application/json, text/`*` 响应体类型日志
 */
fun Response.logString(byteCount: Long = 1024 * 1024 * 4): String? {
    val mediaType = body?.contentType() ?: return null
    return if (mediaType.subtype == "json" || mediaType.type == "text") {
        body?.peekString(byteCount)
    } else {
        "Not support this type $mediaType"
    }
}