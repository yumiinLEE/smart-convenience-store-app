package com.ssafy.finalpass.dto

// 서버에서는 OrderRequest를 받아서 Order로 저장하는 구조로 처리
data class OrderRequest(
    val userId: String,
    val storeName: String,
    val details: List<OrderDetail>
)