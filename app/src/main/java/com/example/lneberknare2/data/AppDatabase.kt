package com.example.lneberknare2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lneberknare2.data.model.JobProfile
import com.example.lneberknare2.data.model.WorkShift

@Database(
    entities = [JobProfile::class, WorkShift::class],
    version = 2, // <-- STEG 1: Öka versionen från 1 till 2
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "salary_tracker_database"
                )
                    // STEG 2: Lägg till denna rad. Den raderar och bygger om databasen
                    // om versionen ändras. Perfekt för utveckling.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

