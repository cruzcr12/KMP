package com.plcoding.bookpedia.book.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * This class is used to convert a list of strings into a string and vice versa.
 * This can be used to store the list of authors or languages in the database without needing to
 * create other entities for those types and link them to the book entity.
 */
object StringListTypeConverter {

    /**
     * Convert a string (JSON object) into a list of strings.
     * @param value The string to convert.
     * @return The list of strings.
     */
    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    /**
     * Convert a list of strings into a string (JSON object).
     */
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(list)
    }

}