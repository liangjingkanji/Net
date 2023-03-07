package com.drake.net.reflect

import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> typeTokenOf() = typeOf<T>().javaType