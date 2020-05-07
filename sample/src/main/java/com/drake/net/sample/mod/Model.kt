package com.drake.net.sample.mod

data class Model(var code: Int, var msg: String, var data: Data) {
    data class Data(var info: String)
}