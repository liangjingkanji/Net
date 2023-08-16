package com.drake.net.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenModel(
    var token: String = "",
    var isExpired: Boolean = false
)