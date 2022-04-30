package com.drake.net.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeBannerModel(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
)