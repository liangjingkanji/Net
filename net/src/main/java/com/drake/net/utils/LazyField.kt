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

package com.drake.net.utils

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 延迟初始化
 * 线程安全
 * 等效于[lazy], 但是可以获取委托字段属性
 */
@Suppress("UNCHECKED_CAST")
fun <T, V> T.lazyField(block: T.(KProperty<*>) -> V) = object : ReadWriteProperty<T, V> {
    @Volatile
    private var value: V? = null
    override fun getValue(thisRef: T, property: KProperty<*>): V {

        return synchronized(this) {
            if (value == null) {
                value = block(thisRef, property)
                value as V
            } else value as V
        }
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        this.value = value
    }
}