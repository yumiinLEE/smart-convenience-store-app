package com.ssafy.finalpass.service

import com.ssafy.finalpass.dto.Store
import retrofit2.Response
import retrofit2.http.GET

interface StoreService {
    // 전체 매장 조회
    @GET("api/stores")
    suspend fun getAllStore(): Response<List<Store>>
}