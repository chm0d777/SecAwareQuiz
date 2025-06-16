package com.chmod777.secawarequiz.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "fake_login_game_items")
@TypeConverters(StringListConverter::class)
data class FakeLoginGameItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serviceName: String,
    val description: String,
    val isFake: Boolean,
    val elementsToSpot: List<String>,
    val explanation: String
)
