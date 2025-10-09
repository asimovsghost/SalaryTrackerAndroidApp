package com.example.lneberknare2.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

/**
 * Talar om för Room hur den ska konvertera komplexa typer (som vi vill använda)
 * till primitiva typer (som den kan spara i databasen).
 */
class Converters {
    // Konverterar från en Long (timestamp) till ett LocalDate-objekt
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    // Konverterar från ett LocalDate-objekt till en Long (timestamp)
    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    // Konverterar från en String till ett LocalTime-objekt
    @TypeConverter
    fun fromString(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    // Konverterar från ett LocalTime-objekt till en String
    @TypeConverter
    fun timeToString(time: LocalTime?): String? {
        return time?.toString()
    }
}