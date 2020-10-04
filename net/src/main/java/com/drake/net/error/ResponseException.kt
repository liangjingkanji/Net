/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.error

import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.exception.NetException

/**
 *  对应网络请求后台定义的错误信息
 * @param msg 网络请求错误信息
 * @param code 网络请求错误码
 * @param tag 应对错误码判断为错时但是后端又返回了需要使用的数据(建议后端修改). 一般在Convert中设置数据
 */
class ResponseException(
    val code: Int,
    val msg: String,
    request: Request
) : NetException(request, "$code $msg")
