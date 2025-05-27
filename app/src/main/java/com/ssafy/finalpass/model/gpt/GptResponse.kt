package com.ssafy.finalpass.model.gpt

data class GptResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
