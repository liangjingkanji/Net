/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：1/15/20 3:15 PM
 */

package com.yanzhenjie.kalle.simple

import com.yanzhenjie.kalle.Headers

data class Result<S, F>(
    var code: Int,
    var headers: Headers,
    var fromCache: Boolean,
    var success: S? = null,
    var failure: F? = null
) {

    val isSucceed
        get() = success != null || failure == null

}