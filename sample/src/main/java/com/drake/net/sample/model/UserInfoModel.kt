package com.drake.net.sample.model


import kotlinx.serialization.Serializable

@Serializable
data class UserInfoModel(
    var userId: Int = 0,
    var username: String = "",
    var age: Int = 0,
    var balance: String = ""
)