package com.example.lneberknare2.ui.screens.calender

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

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
        // TODO: Här kan vi lägga till visuella indikatorer för arbetspass
        Spacer(modifier = Modifier.height(16.dp))
    }
}
