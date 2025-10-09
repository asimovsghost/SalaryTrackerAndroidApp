package com.example.lneberknare2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lneberknare2.data.model.JobProfile

// En lista med fördefinierade färger att välja från
val predefinedColors = listOf(
    Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
    Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
    Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
    Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFF795548)
)

// Funktion för att konvertera en Color till en Hex-sträng
fun Color.toHexString(): String {
    return String.format("#%06X", 0xFFFFFF and this.toArgb())
}

@Composable
fun JobProfileDialog(
    jobProfile: JobProfile? = null, // Null om det är en ny profil, annars den som redigeras
    onDismiss: () -> Unit,
    onSave: (JobProfile) -> Unit
) {
    var name by remember { mutableStateOf(jobProfile?.name ?: "") }
    var wage by remember { mutableStateOf(jobProfile?.hourlyWage?.toString() ?: "") }
    var selectedColor by remember { mutableStateOf(jobProfile?.colorHex ?: predefinedColors.first().toHexString()) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var wageError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        nameError = if (name.isBlank()) "Namn får inte vara tomt" else null
        wageError = if (wage.toDoubleOrNull() == null) "Ogiltig timlön" else null
        return nameError == null && wageError == null
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (jobProfile == null) "Skapa Jobbprofil" else "Redigera Jobbprofil",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = null },
                    label = { Text("Namn på jobb") },
                    isError = nameError != null
                )
                nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = wage,
                    onValueChange = { wage = it; wageError = null },
                    label = { Text("Timlön (kr)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = wageError != null
                )
                wageError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Välj en färg", style = MaterialTheme.typography.bodyLarge)
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 40.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    items(predefinedColors) { color ->
                        val hex = color.toHexString()
                        ColorChip(
                            color = color,
                            isSelected = hex == selectedColor,
                            onClick = { selectedColor = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Avbryt")
                    }
                    Button(onClick = {
                        if (validate()) {
                            val profileToSave = jobProfile?.copy(
                                name = name,
                                hourlyWage = wage.toDouble(),
                                colorHex = selectedColor
                            ) ?: JobProfile(
                                name = name,
                                hourlyWage = wage.toDouble(),
                                colorHex = selectedColor
                            )
                            onSave(profileToSave)
                        }
                    }) {
                        Text("Spara")
                    }
                }
            }
        }
    }
}

@Composable
fun ColorChip(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                else Modifier
            )
    )
}
