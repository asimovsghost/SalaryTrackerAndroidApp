package com.example.lneberknare2.data

import androidx.room.*
import com.example.lneberknare2.data.model.JobProfile
import com.example.lneberknare2.data.model.WorkShift
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object. Definierar alla metoder för att interagera med databasen.
 * Room genererar automatiskt koden för dessa metoder.
 * Att använda Flow<> gör att UI:t automatiskt uppdateras när datan ändras.
 */
@Dao
interface AppDao {
    // --- JobProfile Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobProfile(jobProfile: JobProfile)

    @Update
    suspend fun updateJobProfile(jobProfile: JobProfile)

    @Delete
    suspend fun deleteJobProfile(jobProfile: JobProfile)

    @Query("SELECT * FROM job_profiles ORDER BY name ASC")
    fun getAllJobProfiles(): Flow<List<JobProfile>>

    // --- WorkShift Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkShift(workShift: WorkShift)

    @Update
    suspend fun updateWorkShift(workShift: WorkShift)

    @Delete
    suspend fun deleteWorkShift(workShift: WorkShift)

    @Query("SELECT * FROM work_shifts WHERE date = :date ORDER BY startTime ASC")
    fun getWorkShiftsForDate(date: LocalDate): Flow<List<WorkShift>>
}
