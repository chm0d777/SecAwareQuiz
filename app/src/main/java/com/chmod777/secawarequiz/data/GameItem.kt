package com.chmod777.secawarequiz.data

import androidx.compose.runtime.Immutable

@Immutable
data class GameItem(
    val id: Int,
    val scenario: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)
