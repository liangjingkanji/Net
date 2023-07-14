package com.drake.net.sample.model


import kotlinx.serialization.Serializable

@Serializable
data class ConfigModel(
    var maintain: Boolean = false
)