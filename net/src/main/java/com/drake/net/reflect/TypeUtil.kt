package com.drake.net.reflect

inline fun <reified R> typeTokenOf() = object : TypeToken<R>() {}.type