package com.example.lneberknare2.ui.screens.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lneberknare2.data.AppDao
import com.example.lneberknare2.data.model.WorkShift
import com.example.lneberknare2.services.ISalaryCalculatorService
import com.example.lneberknare2.services.SalaryCalculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val weekDays: List<LocalDate> = emptyList(),
    val monthHeader: String = "",
    val workShiftsForWeek: Map<LocalDate, List<WorkShift>> = emptyMap(),
    val salaryCalculation: SalaryCalculation? = null
)

class CalendarViewModel(
    private val dao: AppDao,
    private val salaryCalculator: ISalaryCalculatorService
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CalendarUiState> = _selectedDate.flatMapLatest { date ->
        val monthStart = date.withDayOfMonth(1)
        val monthEnd = date.with(TemporalAdjusters.lastDayOfMonth())

        val shiftsForMonthFlow = dao.getWorkShiftsForMonth(monthStart, monthEnd)
        val allJobProfilesFlow = dao.getAllJobProfiles()

        combine(
            shiftsForMonthFlow,
            allJobProfilesFlow
        ) { shifts, profiles ->
            val profilesById = profiles.associateBy { it.id }
            var totalGrossSalary = BigDecimal.ZERO

            shifts.forEach { shift ->
                profilesById[shift.jobProfileId]?.let { profile ->
                    val duration = Duration.between(shift.startTime, shift.endTime)
                    val hours = duration.toMinutes() / 60.0
                    val shiftGross = BigDecimal.valueOf(profile.hourlyWage).multiply(BigDecimal.valueOf(hours))
                    totalGrossSalary = totalGrossSalary.add(shiftGross)
                }
            }

            var calculation: SalaryCalculation? = null
            if (totalGrossSalary > BigDecimal.ZERO) {
                // The service calculates gross salary internally. We pass the pre-calculated
                // total as the hourly wage and 1.0 as the hours to get the correct tax calculation.
                calculation = salaryCalculator.calculate(totalGrossSalary, 1.0, 1)
            }

            val weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val weekDays = List(7) { i -> weekStart.plusDays(i.toLong()) }
            val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            val monthHeader = date.format(monthFormatter).replaceFirstChar { it.uppercase() }
            val shiftsByDay = shifts.groupBy { it.date }

            CalendarUiState(
                selectedDate = date,
                weekDays = weekDays,
                monthHeader = monthHeader,
                workShiftsForWeek = shiftsByDay,
                salaryCalculation = calculation
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
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
