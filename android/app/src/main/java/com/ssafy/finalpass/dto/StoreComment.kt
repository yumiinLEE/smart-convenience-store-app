package com.ssafy.finalpass.dto

data class StoreComment(
    val id: Int = -1,
    val userId: String,
    val userName: String,
    val storeId: Int,
    val rating: Float,
    val comment: String
)
