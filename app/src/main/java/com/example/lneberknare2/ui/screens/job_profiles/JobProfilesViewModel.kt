package com.example.lneberknare2.ui.screens.job_profiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lneberknare2.data.AppDao
import com.example.lneberknare2.data.model.JobProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel för JobProfilesScreen.
 * Ansvarar för att förbereda och hantera data för UI:t.
 * Den kommunicerar med databasen (via DAO) och exponerar datan till skärmen.
 */
class JobProfilesViewModel(private val dao: AppDao) : ViewModel() {

    // Hämtar alla jobbprofiler som en "Flow" och gör om den till en "StateFlow".
    // UI:t kan sedan prenumerera på denna och uppdateras automatiskt när datan ändras.
    val jobProfiles: StateFlow<List<JobProfile>> = dao.getAllJobProfiles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L), // Håll data aktiv i 5 sek efter att UI:t slutat lyssna
            initialValue = emptyList()
        )

    fun addJobProfile(name: String, hourlyWage: Double, colorHex: String) {
        viewModelScope.launch {
            val newProfile = JobProfile(name = name, hourlyWage = hourlyWage, colorHex = colorHex)
            dao.insertJobProfile(newProfile)
        }
    }

    fun updateJobProfile(jobProfile: JobProfile) {
        viewModelScope.launch {
            dao.updateJobProfile(jobProfile)
        }
    }

    fun deleteJobProfile(jobProfile: JobProfile) {
        viewModelScope.launch {
            dao.deleteJobProfile(jobProfile)
        }
    }
}
