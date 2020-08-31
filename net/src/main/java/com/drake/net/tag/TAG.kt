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

package com.drake.net.tag

import kotlin.reflect.KClass


abstract class TAG {

    protected var list: MutableList<TAG>? = null

    operator fun get(key: KClass<out TAG>): TAG? {

        list?.forEach {
            if (it::class == key) return it
        }

        return if (this::class == key) this else null
    }

    operator fun plus(tag: TAG): TAG {

        if (this == tag) return this
        if (list == null) list = mutableListOf(this)

        list?.apply {
            add(this@TAG)

            if (tag.list.isNullOrEmpty()) {
                if (tag !in this) add(tag)
            } else {
                addAll(tag.list!!)
            }
        }

        return this
    }

    operator fun minus(tag: TAG): TAG {

        if (this == tag) return this

        val right = tag.list

        if (right.isNullOrEmpty()) {
            list?.remove(tag)
        } else {
            list?.removeAll(right)
        }

        return this
    }

    operator fun contains(tag: @UnsafeVariance TAG): Boolean {

        if (tag == this) return true

        val right = tag.list

        return if (right.isNullOrEmpty()) {
            list?.contains(tag) ?: false
        } else {
            list?.containsAll(right) ?: false
        }
    }

}