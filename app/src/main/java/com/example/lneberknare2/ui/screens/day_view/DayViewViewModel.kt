package com.example.lneberknare2.ui.screens.day_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lneberknare2.data.AppDao
import com.example.lneberknare2.data.model.JobProfile
import com.example.lneberknare2.data.model.WorkShift
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

data class DayViewUiState(
    val selectedDate: LocalDate? = null,
    val workShifts: List<WorkShift> = emptyList(),
    val jobProfiles: List<JobProfile> = emptyList()
)

class DayViewViewModel(private val dao: AppDao) : ViewModel() {

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    private val _uiState = MutableStateFlow(DayViewUiState())
    val uiState: StateFlow<DayViewUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Kombinera flöden för att bygga ett komplett UI-tillstånd
            combine(
                _selectedDate,
                dao.getAllJobProfiles()
            ) { date, profiles ->
                Pair(date, profiles)
            }.flatMapLatest { (date, profiles) ->
                if (date != null) {
                    dao.getWorkShiftsForDate(date).map { shifts ->
                        DayViewUiState(
                            selectedDate = date,
                            workShifts = shifts,
                            jobProfiles = profiles
                        )
                    }
                } else {
                    flowOf(DayViewUiState(jobProfiles = profiles))
                }
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun setDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun addOrUpdateWorkShift(
        shift: WorkShift?,
        jobProfileId: Int,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ) {
        viewModelScope.launch {
            if (shift == null) { // Add new
                dao.insertWorkShift(
                    WorkShift(
                        jobProfileId = jobProfileId,
                        date = date,
                        startTime = startTime,
                        endTime = endTime
                    )
                )
            } else { // Update existing
                dao.updateWorkShift(
                    shift.copy(
                        jobProfileId = jobProfileId,
                        date = date,
                        startTime = startTime,
                        endTime = endTime
                    )
                )
            }
        }
    }

    fun deleteWorkShift(shift: WorkShift) {
        viewModelScope.launch {
            dao.deleteWorkShift(shift)
        }
    }
}
