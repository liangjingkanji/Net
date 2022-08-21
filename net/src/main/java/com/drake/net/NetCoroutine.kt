@file:Suppress("FunctionName")

package com.drake.net

import com.drake.net.internal.NetDeferred
import com.drake.net.request.BodyRequest
import com.drake.net.request.Method
import com.drake.net.request.UrlRequest
import kotlinx.coroutines.*


// <editor-fold desc="异步请求">


/**
 * 异步网络请求
 *
 * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
 * @param tag 可以传递对象给Request, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Get(
    path: String,
    tag: Any? = null,
    noinline block: (UrlRequest.() -> Unit)? = null
): Deferred<M> = NetDeferred(async(Dispatchers.IO + SupervisorJob()) {
    coroutineContext.ensureActive()
    UrlRequest().apply {
        setPath(path)
        method = Method.GET
        setGroup(coroutineContext[CoroutineExceptionHandler])
        tag(tag)
        block?.invoke(this)
    }.execute()
})

/**
 * 异步网络请求
 *
 * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
 * @param tag 可以传递对象给Request, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Post(
    path: String,
    tag: Any? = null,
    noinline block: (BodyRequest.() -> Unit)? = null
): Deferred<M> = NetDeferred(async(Dispatchers.IO + SupervisorJob()) {
    coroutineContext.ensureActive()
    BodyRequest().apply {
        setPath(path)
        method = Method.POST
        setGroup(coroutineContext[CoroutineExceptionHandler])
        tag(tag)
        block?.invoke(this)
    }.execute()
})

/**
 * 异步网络请求
 *
 * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
 * @param tag 可以传递对象给Request, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Head(
    path: String,
    tag: Any? = null,
    noinline block: (UrlRequest.() -> Unit)? = null
): Deferred<M> = NetDeferred(async(Dispatchers.IO + SupervisorJob()) {
    coroutineContext.ensureActive()
    UrlRequest().apply {
        setPath(path)
        method = Method.HEAD
        setGroup(coroutineContext[CoroutineExceptionHandler])
        tag(tag)
        block?.invoke(this)
    }.execute()
})

/**
 * 异步网络请求
 *
 * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
 * @param tag 可以传递对象给Request, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Options(
    path: String,
    tag: Any? = null,
    noinline block: (UrlRequest.() -> Unit)? = null
): Deferred<M> = NetDeferred(async(Dispatchers.IO + SupervisorJob()) {
    coroutineContext.ensureActive()
    UrlRequest().apply {
        setPath(path)
        method = Method.OPTIONS
        setGroup(coroutineContext[CoroutineExceptionHandler])
        tag(tag)
        block?.invoke(this)
    }.execute()
})

/**
 * 异步网络请求
 *
 * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
 * @param tag 可以传递对象给Request, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Trace(
    path: String,
    tag: Any? = null,
    noinline block: (UrlRequest.() -> Unit)? = null
): Deferred<M> = NetDeferred(async(Dispatchers.IO + SupervisorJob()) {
    coroutineContext.ensureActive()
    UrlRequest().apply {
        setPath(path)
        method = Method.TRACE
        setGroup(coroutineContext[CoroutineExceptionHandler])
        tag(tag)
        block?.invoke(this)
    }.execute()
})

/**
 * 异步网络请求
 *
 * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
 * @param tag 可以传递对象给Request, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Delete(
    path: String,
    tag: Any? = null,
    noinline block: (BodyRequest.() -> Unit)? = null
): Deferred<M> = NetDeferred(async(Dispatchers.IO + SupervisorJob()) {
    coroutineContext.ensureActive()
    BodyRequest().apply {
        setPath(path)
        method = Method.DELETE
        setGroup(coroutineContext[CoroutineExceptionHandler])
        tag(tag)
        block?.invoke(this)
    }.execute()
})

/**
 * 异步网络请求
 *
 * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
 * @param tag 可以传递对象给Request, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Put(
    path: String,
    tag: Any? = null,
    noinline block: (BodyRequest.() -> Unit)? = null
): Deferred<M> = NetDeferred(async(Dispatchers.IO + SupervisorJob()) {
    coroutineContext.ensureActive()
    BodyRequest().apply {
        setPath(path)
        method = Method.PUT
        setGroup(coroutineContext[CoroutineExceptionHandler])
        tag(tag)
        block?.invoke(this)
    }.execute()
})

/**
 * 异步网络请求
 *
 * @param path 请求路径, 如果其不包含http/https则会自动拼接[NetConfig.host]
 * @param tag 可以传递对象给Request, 一般用于在拦截器/转换器中进行针对某个接口行为判断
 * @param block 函数中可以配置请求参数
 */
inline fun <reified M> CoroutineScope.Patch(
    path: String,
    tag: Any? = null,
    noinline block: (BodyRequest.() -> Unit)? = null
): Deferred<M> = NetDeferred(async(Dispatchers.IO + SupervisorJob()) {
    coroutineContext.ensureActive()
    BodyRequest().apply {
        setPath(path)
        method = Method.PATCH
        setGroup(coroutineContext[CoroutineExceptionHandler])
        tag(tag)
        block?.invoke(this)
    }.execute()
}
)
// </editor-fold>