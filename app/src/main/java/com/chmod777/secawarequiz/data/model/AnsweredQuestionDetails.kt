package com.chmod777.secawarequiz.data.model

import com.chmod777.secawarequiz.data.QuizQuestion

data class AnsweredQuestionDetails(
    val question: QuizQuestion,
    val userAnswerIndex: Int,
    val wasCorrect: Boolean

)
