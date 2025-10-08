package com.example.lneberknare2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lneberknare2.services.ISalaryCalculatorService
import com.example.lneberknare2.services.SalaryCalculation
import com.example.lneberknare2.services.TaxApproximationService
import com.example.lneberknare2.ui.theme.Löneberäknare2Theme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Löneberäknare2Theme {
                // Scaffold är en grundläggande layoutstruktur från Material Design
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Här anropar vi vår nya funktion som innehåller hela kalkylatorn
                    SalaryCalculatorScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// @Composable betyder att detta är en UI-funktion (en "Composable")
@Composable
fun SalaryCalculatorScreen(modifier: Modifier = Modifier) {
    // Vi skapar en instans av vår beräkningstjänst.
    // I en större app skulle man använda Dependency Injection (Hilt/Koin).
    val salaryCalculator: ISalaryCalculatorService = remember { TaxApproximationService() }

    // 'remember' och 'mutableStateOf' används för att hålla reda på appens tillstånd.
    // När ett 'state' ändras, ritas gränssnittet om automatiskt.
    var hours by remember { mutableStateOf("160") }
    var wage by remember { mutableStateOf("150") }
    var calculationResult by remember { mutableStateOf<SalaryCalculation?>(null) }

    // Column staplar element vertikalt
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lönekalkylator",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Inmatningsfält för timmar
        OutlinedTextField(
            value = hours,
            onValueChange = { hours = it },
            label = { Text("Antal timmar") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Inmatningsfält för timlön
        OutlinedTextField(
            value = wage,
            onValueChange = { wage = it },
            label = { Text("Timlön (kr)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Beräkna-knappen
        Button(
            onClick = {
                val hoursDouble = hours.toDoubleOrNull()
                val wageBigDecimal = wage.toBigDecimalOrNull()
                if (hoursDouble != null && wageBigDecimal != null) {
                    calculationResult = salaryCalculator.calculate(wageBigDecimal, hoursDouble, 1) // Använder kolumn 1 som standard
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Beräkna Lön", fontSize = 16.sp)
        }

        // Om vi har ett resultat, visa resultat-kortet
        calculationResult?.let { result ->
            Spacer(modifier = Modifier.height(24.dp))
            ResultCard(result = result)
        }
    }
}

// En separat Composable för att visa resultatet
@Composable
fun ResultCard(result: SalaryCalculation) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("sv", "SE")).apply {
            maximumFractionDigits = 0
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Resultat",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            ResultRow(label = "Bruttolön:", value = currencyFormat.format(result.grossSalary))
            ResultRow(label = "Uppskattad skatt:", value = "- ${currencyFormat.format(result.taxAmount)}", valueColor = MaterialTheme.colorScheme.error)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            ResultRow(label = "Nettolön:", value = currencyFormat.format(result.netSalary), isHeader = true)
        }
    }
}

// En hjälpreda för att visa en rad i resultat-kortet
@Composable
fun ResultRow(label: String, value: String, isHeader: Boolean = false, valueColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = if(isHeader) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium)
        Text(text = value, style = if(isHeader) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium, color = valueColor)
    }
}


// Den här funktionen låter oss förhandsgranska vår UI i Android Studio
@Preview(showBackground = true)
@Composable
fun SalaryCalculatorScreenPreview() {
    Löneberäknare2Theme {
        SalaryCalculatorScreen()
    }
}