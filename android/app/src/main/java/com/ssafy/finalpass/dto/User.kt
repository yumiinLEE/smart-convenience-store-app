package com.ssafy.finalpass.dto

data class User(
    val id: String,
    val name: String,
    val pass: String,
    val point: Int,
    val payment: Int
)
