package com.drake.net.interfaces

import com.drake.net.NetConfig
import com.drake.net.request.converter
import com.drake.net.utils.runMain
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType

/**
 * 提供泛型转换特性
 * 相对于OkHttp的Callback新增三个回调函数: [onSuccess] [onError] [onComplete]
 * 这三个函数都运行在主线程上
 */
abstract class NetCallback<T> : Callback {

    override fun onResponse(call: Call, response: Response) {
        val succeed = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val data = response.request.converter()?.onConvert<T>(succeed, response) ?: return
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

    /**
     * 请求成功
     */
    abstract fun onSuccess(call: Call, result: T)

    /**
     * 请求错误
     */
    open fun onError(call: Call, e: IOException) = NetConfig.errorHandler.onError(e)

    /**
     * 请求完成
     *
     * @param e 正常结束则为NULL, 发生异常结束则为异常对象
     */
    open fun onComplete(call: Call, e: IOException?) {}
}