package com.chmod777.secawarequiz.data.model

import com.chmod777.secawarequiz.data.GameItem

data class AnsweredGameItemDetails(
    val gameItem: GameItem,
    val userAnswerIndex: Int,
    val wasCorrect: Boolean
)
