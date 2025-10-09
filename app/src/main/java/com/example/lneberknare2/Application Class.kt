package com.example.lneberknare2

import android.app.Application
import com.example.lneberknare2.data.AppDatabase

/**
 * En egen Application-klass för att ha en central plats för
 * globala objekt, som vår databasinstans.
 */
class SalaryTrackerApplication : Application() {
    // Använder "lazy" så att databasen endast skapas när den behövs för första gången.
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
