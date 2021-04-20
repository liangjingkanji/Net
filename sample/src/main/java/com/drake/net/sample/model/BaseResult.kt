package com.drake.net.sample.model

open class BaseResult<T> {
    var code: Int = 0
    var msg: String = ""
    var data: T? = null
}