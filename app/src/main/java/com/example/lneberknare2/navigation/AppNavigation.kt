package com.example.lneberknare2.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lneberknare2.SalaryTrackerApplication
import com.example.lneberknare2.ViewModelFactory
import com.example.lneberknare2.ui.screens.job_profiles.JobProfilesScreen

// Definierar de olika skärmarna/rutterna i appen
object AppRoutes {
    const val JOB_PROFILES = "job_profiles"
    // const val CALENDAR = "calendar" // Kommer sen!
}

@Composable
fun AppNavigation(application: SalaryTrackerApplication) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.JOB_PROFILES) {
        composable(AppRoutes.JOB_PROFILES) {
            JobProfilesScreen(
                viewModel = viewModel(factory = ViewModelFactory(application))
            )
        }
        // Här kommer vi lägga till fler "composable" för andra skärmar
    }
}
