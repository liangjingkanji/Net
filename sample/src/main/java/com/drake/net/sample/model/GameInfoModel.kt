package com.drake.net.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class GameInfoModel(
    var id: Int = 0,
    var name: String = "",
    var desc: String = "", // 该字段在接口数据中不存在, 但是由于存在默认值不会影响正常解析
)