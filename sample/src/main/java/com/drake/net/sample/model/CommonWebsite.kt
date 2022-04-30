package com.drake.net.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class CommonWebsite(
    var category: String = "",
    var icon: String = "",
    var id: Int = 0,
    var link: String = "",
    var name: String = "",
    var order: Int = 0,
    var visible: Int = 0
)