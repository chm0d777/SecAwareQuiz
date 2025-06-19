package com.chmod777.secawarequiz.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "quiz_questions")
@TypeConverters(StringListConverter::class)
data class QuizQuestion(
    @PrimaryKey val id: Int,
    val quizGroupId: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)
