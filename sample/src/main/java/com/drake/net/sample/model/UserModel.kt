package com.drake.net.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(var name: String = "", var age: Int = 0, var height: Int = 0)