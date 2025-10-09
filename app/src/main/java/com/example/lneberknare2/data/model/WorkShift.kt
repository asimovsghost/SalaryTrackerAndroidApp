package com.example.lneberknare2.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

/**
 * Representerar ett enskilt arbetspass i databasen.
 *
 * @property id Unikt ID för arbetspasset.
 * @property jobProfileId ID för den jobbprofil som passet tillhör. Detta skapar en koppling (foreign key).
 * @property date Datumet för arbetspasset.
 * @property startTime Starttiden för passet.
 * @property endTime Sluttiden för passet.
 */
@Entity(
    tableName = "work_shifts",
    foreignKeys = [ForeignKey(
        entity = JobProfile::class,
        parentColumns = ["id"],
        childColumns = ["job_profile_id"],
        onDelete = ForeignKey.CASCADE // Om en jobbprofil tas bort, tas även dess arbetspass bort.
    )]
)
data class WorkShift(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "job_profile_id", index = true)
    val jobProfileId: Int,

    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)

