package com.example.lneberknare2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representerar en tabell i databasen för jobbprofiler.
 * Varje instans av denna klass är en rad i tabellen.
 *
 * @param id Unik identifierare för varje jobbprofil. Skapas automatiskt.
 * @param name Användarens namn på jobbet (t.ex. "Lagerjobb", "Restaurang").
 * @param hourlyWage Timlönen för detta jobb.
 * @param colorHex En färgkod (t.ex. "#FF5733") för att visuellt särskilja jobbet i UI.
 */
@Entity(tableName = "job_profiles")
data class JobProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val hourlyWage: Double,
    val colorHex: String
)
