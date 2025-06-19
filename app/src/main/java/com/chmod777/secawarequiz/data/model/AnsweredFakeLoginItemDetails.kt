package com.chmod777.secawarequiz.data.model

import com.chmod777.secawarequiz.data.FakeLoginGameItem

data class AnsweredFakeLoginItemDetails(
    val fakeLoginItem: FakeLoginGameItem,
    val userAnswerWasFake: Boolean,
    val wasCorrect: Boolean
)
