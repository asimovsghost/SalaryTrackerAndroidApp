package com.example.lneberknare2.ui.screens.calender

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lneberknare2.services.SalaryCalculation
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onDayClick: (LocalDate) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        CalendarHeader(
            monthHeader = uiState.monthHeader,
            onPreviousClick = { viewModel.previousWeek() },
            onNextClick = { viewModel.nextWeek() },
            onTodayClick = { viewModel.goToToday() }
        )
        WeekView(
            weekDays = uiState.weekDays,
            onDayClick = onDayClick
        )
        uiState.salaryCalculation?.let {
            SalarySummaryCard(it)
        }
    }
}

@Composable
fun CalendarHeader(
    monthHeader: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onTodayClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Föregående vecka")
        }
        TextButton(onClick = onTodayClick) {
            Text(
                text = monthHeader,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = onNextClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Nästa vecka")
        }
    }
}

@Composable
fun WeekView(
    weekDays: List<LocalDate>,
    onDayClick: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (day in weekDays) {
            DayCell(
                day = day,
                isToday = day.isEqual(LocalDate.now()),
                onClick = { onDayClick(day) }
            )
        }
    }
}

@Composable
fun DayCell(
    day: LocalDate,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val dayOfWeekName = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("sv", "SE"))
    val dayOfMonth = day.dayOfMonth.toString()

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .width(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = dayOfWeekName, style = MaterialTheme.typography.bodySmall)

        Box(
            modifier = Modifier
                .size(32.dp)
                .then(
                    if (isToday) Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfMonth,
                fontWeight = FontWeight.Bold,
                color = if (isToday) MaterialTheme.colorScheme.onPrimary else Color.Unspecified,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SalarySummaryCard(calculation: SalaryCalculation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Lönesammanfattning", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Bruttolön:")
                Text("${calculation.grossSalary} kr")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Skatt:")
                Text("${calculation.taxAmount} kr")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Nettolön:", fontWeight = FontWeight.Bold)
                Text("${calculation.netSalary} kr", fontWeight = FontWeight.Bold)
            }
        }
    }
}
