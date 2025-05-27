package com.ssafy.finalpass.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GPTClient {
    val gptService: GptService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GptService::class.java)
    }
}