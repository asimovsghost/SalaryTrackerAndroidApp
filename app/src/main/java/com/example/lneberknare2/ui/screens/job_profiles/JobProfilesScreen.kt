package com.example.lneberknare2.ui.screens.job_profiles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lneberknare2.data.model.JobProfile
import com.example.lneberknare2.ui.components.JobProfileDialog
import java.text.NumberFormat
import java.util.*

@Composable
fun JobProfilesScreen(
    viewModel: JobProfilesViewModel
) {
    val jobProfiles by viewModel.jobProfiles.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var profileToEdit by remember { mutableStateOf<JobProfile?>(null) }

    // Den yttre Scaffold har flyttats till AppNavigation
    Box(modifier = Modifier.fillMaxSize()) {
        if (showDialog) {
            JobProfileDialog(
                jobProfile = profileToEdit,
                onDismiss = { showDialog = false },
                onSave = {
                    if (it.id == 0) {
                        viewModel.addJobProfile(it.name, it.hourlyWage, it.colorHex)
                    } else {
                        viewModel.updateJobProfile(it)
                    }
                    showDialog = false
                }
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(jobProfiles) { profile ->
                JobProfileItem(
                    profile = profile,
                    onEdit = {
                        profileToEdit = profile
                        showDialog = true
                    },
                    onDelete = { viewModel.deleteJobProfile(profile) }
                )
            }
        }

        FloatingActionButton(
            onClick = {
                profileToEdit = null
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Lägg till jobbprofil")
        }
    }
}


@Composable
fun JobProfileItem(
    profile: JobProfile,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("sv", "SE")).apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(android.graphics.Color.parseColor(profile.colorHex)))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(profile.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text("${currencyFormat.format(profile.hourlyWage)}/tim")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Ta bort profil")
            }
        }
    }
}

