/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：6/23/20 4:32 PM
 */

package com.drake.net.sample.mod

data class ListModel(var code: Int, var msg: String, var data: Data) {
    data class Data(var total: Int, var list: List<String>)
}