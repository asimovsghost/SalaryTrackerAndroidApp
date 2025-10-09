package com.example.lneberknare2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.lneberknare2.navigation.AppNavigation
import com.example.lneberknare2.ui.theme.Löneberäknare2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Löneberäknare2Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Starta appens navigation.
                    // Vi hämtar application-instansen för att kunna skicka med den
                    // till vår ViewModelFactory.
                    val application = application as SalaryTrackerApplication
                    AppNavigation(application)
                }
            }
        }
    }
}
