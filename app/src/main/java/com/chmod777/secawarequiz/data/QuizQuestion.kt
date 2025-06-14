package com.chmod777.secawarequiz.data

data class QuizQuestion(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)
