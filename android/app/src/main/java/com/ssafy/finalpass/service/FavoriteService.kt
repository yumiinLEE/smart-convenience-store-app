package com.ssafy.finalpass.service

import com.ssafy.finalpass.dto.FavoriteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteService {

    // 찜 추가
    @POST("api/favorites")
    suspend fun addFavorite(@Body req: FavoriteRequest): Response<Void>

    // 찜 삭제
    @HTTP(method = "DELETE", path = "api/favorites", hasBody = true)
    suspend fun removeFavorite(@Body req: FavoriteRequest): Response<Void>

    // 사용자 찜 목록 조회 (productId 리스트 반환)
    @GET("api/favorites/{userId}")
    suspend fun getFavorites(@Path("userId") userId: String): Response<List<Int>>
}