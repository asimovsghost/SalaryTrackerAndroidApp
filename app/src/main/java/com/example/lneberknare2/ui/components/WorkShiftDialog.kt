package com.example.lneberknare2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lneberknare2.data.model.JobProfile
import com.example.lneberknare2.data.model.WorkShift
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkShiftDialog(
    workShift: WorkShift?,
    jobProfiles: List<JobProfile>,
    onDismiss: () -> Unit,
    onSave: (jobProfileId: Int, startTime: LocalTime, endTime: LocalTime) -> Unit
) {
    if (jobProfiles.isEmpty()) {
        // Visa ett felmeddelande eller en platshållare om inga jobbprofiler finns
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Inga Jobbprofiler") },
            text = { Text("Du måste skapa en jobbprofil först innan du kan lägga till ett arbetspass.") },
            confirmButton = {
                TextButton(onClick = onDismiss) { Text("OK") }
            }
        )
        return
    }

    var selectedJobProfile by remember { mutableStateOf(jobProfiles.find { it.id == workShift?.jobProfileId } ?: jobProfiles.first()) }
    var startTimeString by remember { mutableStateOf(workShift?.startTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "08:00") }
    var endTimeString by remember { mutableStateOf(workShift?.endTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "17:00") }
    var isJobDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (workShift == null) "Lägg till arbetspass" else "Redigera arbetspass") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Jobbprofil-dropdown
                ExposedDropdownMenuBox(
                    expanded = isJobDropdownExpanded,
                    onExpandedChange = { isJobDropdownExpanded = !isJobDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedJobProfile.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Jobb") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isJobDropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isJobDropdownExpanded,
                        onDismissRequest = { isJobDropdownExpanded = false }
                    ) {
                        jobProfiles.forEach { profile ->
                            DropdownMenuItem(
                                text = { Text(profile.name) },
                                onClick = {
                                    selectedJobProfile = profile
                                    isJobDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Tidsinmatning
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startTimeString,
                        onValueChange = { startTimeString = it },
                        label = { Text("Starttid") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endTimeString,
                        onValueChange = { endTimeString = it },
                        label = { Text("Sluttid") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("HH:mm"))
                    val endTime = LocalTime.parse(endTimeString, DateTimeFormatter.ofPattern("HH:mm"))
                    onSave(selectedJobProfile.id, startTime, endTime)
                }
            ) { Text("Spara") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Avbryt") }
        }
    )
}
