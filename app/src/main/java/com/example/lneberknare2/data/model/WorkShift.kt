package com.example.lneberknare2.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

/**
 * Representerar ett arbetspass.
 * Har en främmande nyckel (foreign key) till JobProfile,
 * vilket innebär att varje arbetspass måste tillhöra en specifik jobbprofil.
 * Om en jobbprofil raderas, raderas även alla tillhörande arbetspass (onDelete = CASCADE).
 *
 * @param id Unik identifierare för arbetspasset.
 * @param jobId ID för den jobbprofil som passet tillhör.
 * @param date Datumet för arbetspasset.
 * @param startTime När passet började.
 * @param endTime När passet slutade.
 */
@Entity(
    tableName = "work_shifts",
    foreignKeys = [ForeignKey(
        entity = JobProfile::class,
        parentColumns = ["id"],
        childColumns = ["jobId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WorkShift(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val jobId: Int,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)
