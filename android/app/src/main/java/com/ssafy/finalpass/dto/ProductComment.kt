package com.ssafy.finalpass.dto

data class ProductComment(
    val id: Int? = null,
    val userId: String,
    val productId: Int,
    val rating: Float,
    val comment: String
)
