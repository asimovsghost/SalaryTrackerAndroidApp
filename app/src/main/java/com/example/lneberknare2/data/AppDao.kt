package com.example.lneberknare2.data

import androidx.room.*
import com.example.lneberknare2.data.model.JobProfile
import com.example.lneberknare2.data.model.WorkShift
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface AppDao {
    // --- JobProfile Queries ---
    @Query("SELECT * FROM job_profiles ORDER BY name ASC")
    fun getAllJobProfiles(): Flow<List<JobProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobProfile(jobProfile: JobProfile)

    @Update
    suspend fun updateJobProfile(jobProfile: JobProfile)

    @Delete
    suspend fun deleteJobProfile(jobProfile: JobProfile)

    // --- WorkShift Queries ---
    @Query("SELECT * FROM work_shifts WHERE date = :date ORDER BY startTime ASC")
    fun getWorkShiftsForDate(date: LocalDate): Flow<List<WorkShift>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkShift(workShift: WorkShift)

    @Update
    suspend fun updateWorkShift(workShift: WorkShift)

    @Delete
    suspend fun deleteWorkShift(workShift: WorkShift)
}

