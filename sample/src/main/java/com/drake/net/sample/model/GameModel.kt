package com.drake.net.sample.model

class GameModel(
    var total: Int = 23,
    var firstName: String = "ssss",
    var list: List<Data> = listOf()
) {

    val lastName = "吴彦祖"

    data class Data(
        var id: Int = 0,
        var img: String = "",
        var name: String = "",
        var label: List<String> = listOf(),
        var price: String = "",
        var initialPrice: String = "",
        var grade: Int = 0,
        var discount: Double = 0.0,
        var endTime: Int = 0
    )
}