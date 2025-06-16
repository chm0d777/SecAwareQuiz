package com.chmod777.secawarequiz.data

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(";;;")
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(";;;")
    }
}
