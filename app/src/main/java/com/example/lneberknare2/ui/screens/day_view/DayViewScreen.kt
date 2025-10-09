package com.example.lneberknare2.ui.screens.day_view

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
import com.example.lneberknare2.data.model.WorkShift
import com.example.lneberknare2.ui.components.WorkShiftDialog
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun DayViewScreen(
    viewModel: DayViewViewModel,
    selectedDate: LocalDate
) {
    // Sätt datumet i ViewModel när skärmen visas första gången
    LaunchedEffect(selectedDate) {
        viewModel.setDate(selectedDate)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var shiftToEdit by remember { mutableStateOf<WorkShift?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showDialog) {
            WorkShiftDialog(
                workShift = shiftToEdit,
                jobProfiles = uiState.jobProfiles,
                onDismiss = { showDialog = false },
                onSave = { jobProfileId, startTime, endTime ->
                    viewModel.addOrUpdateWorkShift(shiftToEdit, jobProfileId, selectedDate, startTime, endTime)
                    showDialog = false
                }
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            val dayFormatter = DateTimeFormatter.ofPattern("eeee d MMMM", Locale("sv", "SE"))
            Text(
                text = selectedDate.format(dayFormatter).replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.workShifts) { shift ->
                    val jobProfile = uiState.jobProfiles.find { it.id == shift.jobProfileId }
                    if (jobProfile != null) {
                        WorkShiftItem(
                            shift = shift,
                            jobProfileName = jobProfile.name,
                            jobProfileColor = Color(android.graphics.Color.parseColor(jobProfile.colorHex)),
                            onEdit = {
                                shiftToEdit = shift
                                showDialog = true
                            },
                            onDelete = { viewModel.deleteWorkShift(shift) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                shiftToEdit = null
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Lägg till arbetspass")
        }
    }
}


@Composable
fun WorkShiftItem(
    shift: WorkShift,
    jobProfileName: String,
    jobProfileColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val duration = Duration.between(shift.startTime, shift.endTime)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60

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
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(jobProfileColor)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(jobProfileName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(
                    "${shift.startTime.format(timeFormatter)} - ${shift.endTime.format(timeFormatter)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "$hours h $minutes min",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Ta bort pass")
            }
        }
    }
}
