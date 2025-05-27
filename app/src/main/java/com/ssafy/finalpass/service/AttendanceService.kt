package com.ssafy.finalpass.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AttendanceService {
    @GET("api/attendance/history/{userId}")
    suspend fun getHistory(@Path("userId") userId: String): Response<List<String>>

    @POST("api/attendance/check/{userId}")
    suspend fun check(@Path("userId") userId: String): Response<String>
}
