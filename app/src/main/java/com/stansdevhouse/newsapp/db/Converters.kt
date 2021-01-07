package com.stansdevhouse.newsapp.db

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromTypeAttributes(typeAttributes: DbTypeAttributes?): String? {
        return if (typeAttributes == null) null else Json.encodeToString(typeAttributes)
    }

    @TypeConverter
    fun stringToTypeAttribute(string: String?): DbTypeAttributes? {
        return string?.let {
            Json.decodeFromString(string)
        }
    }
}