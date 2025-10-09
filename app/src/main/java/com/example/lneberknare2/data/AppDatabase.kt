package com.example.lneberknare2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lneberknare2.data.model.JobProfile
import com.example.lneberknare2.data.model.WorkShift

/**
 * Huvudklassen för databasen. Knyter samman allt:
 * - entities: Vilka tabeller som ska finnas.
 * - version: Databasens version. Måste höjas vid schemaändringar.
 * - TypeConverters: Talar om vilka konverterare som ska användas.
 */
@Database(entities = [JobProfile::class, WorkShift::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    // Companion object med singleton-mönster för att säkerställa
    // att endast en instans av databasen finns i hela appen.
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "salary_tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
