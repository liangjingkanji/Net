package com.drake.net.interfaces

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

/**
 * 提供泛型转换特性
 * 相对于OkHttp的Callback新增三个回调函数: [onSuccess] [onError] [onComplete]
 * 这三个函数都运行在主线程上
 */
abstract class NetCallback<T> constructor(
    val lifecycle: LifecycleOwner? = null,
    val lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) : Callback {

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
        runMain {
            onError(call, e)
            onComplete(call, e)
        }
    }

    open fun onStart(request: Request) {
        request.group = this
        lifecycle?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) Net.cancelGroup(request.group)
            }
        })
    }

    abstract fun onSuccess(call: Call, result: T)

    open fun onError(call: Call, e: IOException) = NetConfig.errorHandler.onError(e)

    /**
     * @param e 正常结束则为NULL, 发生异常结束则为异常对象
     */
    open fun onComplete(call: Call, e: IOException?) {}
}