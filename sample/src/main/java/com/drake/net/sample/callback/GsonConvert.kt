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

package com.drake.net.sample.callback

import com.drake.net.convert.DefaultConvert
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class GsonConvert : DefaultConvert(code = "code", message = "msg", success = "200") {

    override fun <S> String.parseBody(succeed: Type): S? {
        return GsonBuilder().serializeNulls().create().fromJson(this, succeed)
    }
}