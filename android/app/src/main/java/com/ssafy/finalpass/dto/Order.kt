package com.ssafy.finalpass.dto

data class Order(
    val id: Int,
    val userId: String,
    val storeName: String,
    val orderTime: String,
    val details: ArrayList<OrderDetail> = ArrayList()
) {
    constructor(): this(0, "", "", "")
}
