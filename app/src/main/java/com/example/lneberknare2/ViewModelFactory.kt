package com.example.lneberknare2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lneberknare2.services.TaxApproximationService
import com.example.lneberknare2.ui.screens.calender.CalendarViewModel
import com.example.lneberknare2.ui.screens.day_view.DayViewViewModel
import com.example.lneberknare2.ui.screens.job_profiles.JobProfilesViewModel

class ViewModelFactory(private val application: SalaryTrackerApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JobProfilesViewModel::class.java) -> {
                JobProfilesViewModel(application.database.appDao()) as T
            }
            modelClass.isAssignableFrom(CalendarViewModel::class.java) -> {
                CalendarViewModel(application.database.appDao(), TaxApproximationService()) as T
            }
            modelClass.isAssignableFrom(DayViewViewModel::class.java) -> {
                DayViewViewModel(application.database.appDao()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

