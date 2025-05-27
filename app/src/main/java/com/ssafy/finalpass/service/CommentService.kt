package com.ssafy.finalpass.service

import com.ssafy.finalpass.dto.ProductComment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CommentService {

    // 상품 리뷰 등록
    @POST("api/comments")
    suspend fun postComment(@Body comment: ProductComment): Response<Void>

    // 상품id로 해당상품의 전체 리뷰 조회
    @GET("api/comments/{productId}")
    suspend fun getComments(@Path("productId") productId: Int): List<ProductComment>

    // 사용자 ID로 해당 사용자의 전체 리뷰 조회
    @GET("api/comments/user/{userId}")
    suspend fun getCommentsByUser(@Path("userId") userId: String): Response<List<ProductComment>>

    // 리뷰 삭제
    @DELETE("api/comments/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Int): Response<Void>

    // 리뷰 수정
    @PUT("api/comments")
    suspend fun updateComment(@Body comment: ProductComment): Response<Void>

    // 리뷰 전체
    @GET("api/comments")
    suspend fun getAllComments(): Response<List<ProductComment>>
}