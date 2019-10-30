/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：10/30/19 8:15 PM
 */

package com.drake.net.error


/**
 * JSON结构不符
 * 一般情况下属于code和msg无法获取
 */
class JsonStructureException(val msg: String) : Throwable() {
}