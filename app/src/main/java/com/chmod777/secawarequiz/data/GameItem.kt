package com.chmod777.secawarequiz.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "phishing_game_items")
@TypeConverters(StringListConverter::class)
data class GameItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val scenario: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)
