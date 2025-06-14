package com.chmod777.secawarequiz.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_check_questions")
data class UrlCheckQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val isSecure: Boolean,
    val explanation: String
)
