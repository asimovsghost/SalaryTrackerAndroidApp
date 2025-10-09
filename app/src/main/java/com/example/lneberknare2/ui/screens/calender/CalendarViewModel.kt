package com.example.lneberknare2.ui.screens.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lneberknare2.data.AppDao
import com.example.lneberknare2.data.model.WorkShift
import kotlinx.coroutines.flow.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val weekDays: List<LocalDate> = emptyList(),
    val monthHeader: String = "",
    val workShiftsForWeek: Map<LocalDate, List<WorkShift>> = emptyMap()
)

class CalendarViewModel(private val dao: AppDao) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CalendarUiState> = _selectedDate.flatMapLatest { date ->
        val weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val weekDays = List(7) { i -> weekStart.plusDays(i.toLong()) }
        val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        val monthHeader = date.format(monthFormatter).replaceFirstChar { it.uppercase() }

        // Vi hämtar arbetspass för varje dag i veckan
        // I en mer komplex app hade man kanske hämtat allt på en gång
        val shiftsFlows = weekDays.map { day ->
            dao.getWorkShiftsForDate(day).map { shifts -> day to shifts }
        }

        combine(shiftsFlows) { shiftsPerDay ->
            CalendarUiState(
                selectedDate = date,
                weekDays = weekDays,
                monthHeader = monthHeader,
                workShiftsForWeek = shiftsPerDay.toMap()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun nextWeek() {
        _selectedDate.value = _selectedDate.value.plusWeeks(1)
    }

    fun previousWeek() {
        _selectedDate.value = _selectedDate.value.minusWeeks(1)
    }

    fun goToToday() {
        _selectedDate.value = LocalDate.now()
    }
}
