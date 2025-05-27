package com.ssafy.finalpass.model.gpt

data class GptRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    val temperature: Double = 0.7
)

data class Message(
    val role: String, // "user", "system", "assistant"
    val content: String
)
