package com.example.lneberknare2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lneberknare2.ui.screens.job_profiles.JobProfilesViewModel

/**
 * En fabriksklass för att skapa ViewModel-instanser.
 * Detta är standardpraxis i Android för att kunna skicka med beroenden
 * (som vår databas-dao) till ViewModel-konstruktorn.
 */
class ViewModelFactory(private val application: SalaryTrackerApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobProfilesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobProfilesViewModel(application.database.appDao()) as T
        }
        // Lägg till fler ViewModels här i framtiden med "if"-satser.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
