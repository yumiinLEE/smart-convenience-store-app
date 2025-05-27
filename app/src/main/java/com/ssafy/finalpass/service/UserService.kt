package com.ssafy.finalpass.service

import com.ssafy.finalpass.dto.User
import com.ssafy.finalpass.dto.UserRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    // 회원가입
    @POST("api/users/register")
    suspend fun registerUser(@Body user: UserRequest): Response<ResponseBody>

    // 로그인
    @POST("api/users/login")
    suspend fun login(@Body userRequest: UserRequest): Response<User>

    // id로 사용자 정보 조회
    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: String): User

    // 닉네임 수정
    @PUT("api/users/{id}/name")
    suspend fun updateUserName(
        @Path("id") id: String,
        @Body nameMap: Map<String, String>
    ): User

}