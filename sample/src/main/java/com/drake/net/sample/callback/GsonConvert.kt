/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/21/20 12:24 PM
 */

package com.drake.net.sample.callback

import com.drake.net.convert.DefaultConvert
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class GsonConvert : DefaultConvert(code = "code", message = "msg") {

    override fun <S> String.parseBody(succeed: Type): S? {
        return GsonBuilder().serializeNulls().create().fromJson(this, succeed)
    }
}