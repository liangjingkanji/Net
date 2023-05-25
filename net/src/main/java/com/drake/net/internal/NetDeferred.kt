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

package com.drake.net.internal

import com.drake.net.exception.NetException
import com.drake.net.exception.URLParseException
import kotlinx.coroutines.Deferred

@PublishedApi
internal class NetDeferred<M>(private val deferred: Deferred<M>) : Deferred<M> by deferred {

    override suspend fun await(): M {
        // 追踪到网络请求异常发生位置
        val occurred = Throwable().stackTrace.getOrNull(1)?.run { " ...(${fileName}:${lineNumber})" }
        return try {
            deferred.await()
        } catch (e: Exception) {
            when {
                occurred != null && e is NetException -> e.occurred = occurred
                occurred != null && e is URLParseException -> e.occurred = occurred
            }
            throw  e
        }
    }
}