package com.alagamb.petcompose.data.db

import androidx.room.TypeConverter
class Converters {

    @TypeConverter
    fun listToJson(value: List<Int>?): String? =
        value?.joinToString(separator = ",", prefix = "[", postfix = "]")

    @TypeConverter
    fun jsonToList(value: String): List<Int> {
        val json = value.trim()
        if (json == "null") return emptyList()
        return json.removeSurrounding("[", "]")
            .split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
    }
}
