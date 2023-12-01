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

package com.drake.net.tag

import com.drake.net.interfaces.ProgressListener
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KType

sealed class NetTag {
    class Extras : HashMap<String, Any?>()
    class UploadListeners : ConcurrentLinkedQueue<ProgressListener>()
    class DownloadListeners : ConcurrentLinkedQueue<ProgressListener>()

    @JvmInline
    value class RequestId(val value: Any)

    @JvmInline
    value class RequestGroup(val value: Any)

    @JvmInline
    value class RequestKType(val value: KType)

    @JvmInline
    value class DownloadFileMD5Verify(val value: Boolean = true)

    @JvmInline
    value class DownloadFileNameDecode(val value: Boolean = true)

    @JvmInline
    value class DownloadTempFile(val value: Boolean = true)

    @JvmInline
    value class DownloadFileConflictRename(val value: Boolean = true)

    @JvmInline
    value class DownloadFileName(val value: String)

    @JvmInline
    value class CacheComparison(val cacheComparison: () -> File)

    @JvmInline
    value class CacheKey(val value: String)

    @JvmInline
    value class CacheValidTime(val value: Long)

    @JvmInline
    value class DownloadFileDir(val value: String) {
        constructor(fileDir: File) : this(fileDir.absolutePath)
    }
}
