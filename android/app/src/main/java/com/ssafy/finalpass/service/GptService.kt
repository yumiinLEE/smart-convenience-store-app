package com.ssafy.finalpass.service

import com.ssafy.finalpass.BuildConfig
import com.ssafy.finalpass.model.gpt.GptRequest
import com.ssafy.finalpass.model.gpt.GptResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GptService {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Body request: GptRequest,
        @Header("Authorization") auth: String
    ): Response<GptResponse>
}
