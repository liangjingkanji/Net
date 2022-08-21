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