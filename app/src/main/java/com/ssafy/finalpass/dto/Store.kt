package com.ssafy.finalpass.dto

data class Store(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val comment: ArrayList<StoreComment> = ArrayList()
) {
    constructor(): this(0, "", 0f, 0f)
}
