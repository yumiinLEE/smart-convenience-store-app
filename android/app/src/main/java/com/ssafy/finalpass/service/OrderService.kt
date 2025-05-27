package com.ssafy.finalpass.service

import com.ssafy.finalpass.dto.Order
import com.ssafy.finalpass.dto.OrderDetail
import com.ssafy.finalpass.dto.OrderRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {

    // 상품 주문
    @POST("api/orders")
    suspend fun submitOrder(@Body order: OrderRequest): Response<Order>

    // 유저의 상품주문 전체 내역
    @GET("api/orders/{userId}")
    suspend fun getUserOrders(@Path("userId") userId: String): Response<List<Order>>

    // 유저의 상품주문 내역 기간 필터링
    @GET("api/orders/user/{userId}/filter")
    suspend fun getUserOrdersByPeriod(
        @Path("userId") userId: String,
        @Query("months") months: Int
    ): Response<List<Order>>
}