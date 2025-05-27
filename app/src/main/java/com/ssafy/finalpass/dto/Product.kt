package com.ssafy.finalpass.dto

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val img: String,
    val comment: List<ProductComment> = ArrayList(),
    var count: Int = 1 // 장바구니 표시용도로 추가함
)
