package com.drake.net.interfaces

import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.drake.net.Net
import com.drake.net.NetConfig
import com.drake.net.request.group
import com.drake.net.response.convert
import com.drake.net.utils.runMain
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.util.concurrent.CancellationException

/**
 * 提供泛型转换特性
 * 相对于OkHttp的Callback新增三个回调函数: [onSuccess] [onError] [onComplete]
 * 这三个函数都运行在主线程上
 *
 * @param lifecycle 将Activity/Fragment/LifecycleOwner等作为参数传递会在其生命周期结束时自动取消网络请求. 避免内存泄漏
 * @param lifeEvent 指定自动取消的生命周期. 默认为[Lifecycle.Event.ON_DESTROY]
 */
abstract class NetCallback<T> constructor(
    val lifecycle: LifecycleOwner? = null,
    val lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) : Callback {

    lateinit var request: Request
        internal set

    override fun onResponse(call: Call, response: Response) {
        val succeed = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val data = try {
            response.convert<T>(succeed)
        } catch (e: IOException) {
            onFailure(call, e)
            return
        }
        runMain {
            onSuccess(call, data)
            onComplete(call, null)
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        val message = e.cause?.message
        if (message == "Socket closed") {
            runMain { onComplete(call, CancellationException(message)) }
        } else {
            runMain {
                onError(call, e)
                onComplete(call, e)
            }
        }
    }

    /**
     * 网络请求开始
     */
    @MainThread
    open fun onStart(request: Request) {
        request.group = this
        lifecycle?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) Net.cancelGroup(request.group)
            }
        })
    }

    /**
     * 网络请求成功
     *
     * @param result 请求结果
     */
    @MainThread
    abstract fun onSuccess(call: Call, result: T)

    /**
     * 网络请求错误
     */
    @MainThread
    open fun onError(call: Call, e: IOException) = NetConfig.errorHandler.onError(e)

    /**
     * 网络请求完成, 无论错误还是正常结束
     *
     * @param e 正常结束则为null, 发生异常结束则为异常对象. 但是网络请求被取消则为[CancellationException], 且不会回调[onError]
     */
    @MainThread
    open fun onComplete(call: Call, e: Throwable?) {
    }
}