package com.example.lneberknare2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.lneberknare2.SalaryTrackerApplication
import com.example.lneberknare2.ViewModelFactory
import com.example.lneberknare2.ui.screens.calender.CalendarScreen
import com.example.lneberknare2.ui.screens.day_view.DayViewScreen
import com.example.lneberknare2.ui.screens.job_profiles.JobProfilesScreen
import java.time.LocalDate

sealed class Screen(val route: String, val label: String? = null, val icon: ImageVector? = null) {
    object Calendar : Screen("calendar", "Kalender", Icons.Default.CalendarMonth)
    object JobProfiles : Screen("job_profiles", "Profiler", Icons.Default.Person)
    object DayView : Screen("day_view/{date}") // Ingen label/ikon, inte i bottenmenyn
}

val bottomBarItems = listOf(
    Screen.Calendar,
    Screen.JobProfiles,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(application: SalaryTrackerApplication) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lönekalkylatorn") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    // Visa tillbaka-pil om vi inte är på en startskärm
                    if (currentDestination?.route != Screen.Calendar.route && currentDestination?.route != Screen.JobProfiles.route) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tillbaka")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomBarItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon!!, contentDescription = null) },
                        label = { Text(screen.label!!) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calendar.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    viewModel = viewModel(factory = ViewModelFactory(application)),
                    onDayClick = { date ->
                        navController.navigate("${Screen.DayView.route.replace("{date}", date.toString())}")
                    }
                )
            }
            composable(Screen.JobProfiles.route) {
                JobProfilesScreen(
                    viewModel = viewModel(factory = ViewModelFactory(application))
                )
            }
            composable(
                route = Screen.DayView.route,
                arguments = listOf(navArgument("date") { type = NavType.StringType })
            ) { backStackEntry ->
                val dateString = backStackEntry.arguments?.getString("date")
                if (dateString != null) {
                    val selectedDate = LocalDate.parse(dateString)
                    DayViewScreen(
                        viewModel = viewModel(factory = ViewModelFactory(application)),
                        selectedDate = selectedDate
                    )
                }
            }
        }
    }
}

