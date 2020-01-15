/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：1/14/20 1:38 PM
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