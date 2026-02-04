package com.example.mob21project.ui.screens.classSessions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.LoadingManager
import com.example.mob21project.data.model.Booking
import com.example.mob21project.data.model.ClassDetails
import com.example.mob21project.data.model.ClassSession
import com.example.mob21project.data.repo.ActivitiesRepo
import com.example.mob21project.service.AuthService
import com.example.mob21project.ui.utils.startOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

@HiltViewModel
class ClassSessionsViewModel @Inject constructor(
    private val repo: ActivitiesRepo,
    private val savedStateHandle: SavedStateHandle,
    private val authService: AuthService
): ViewModel() {
    private val classId: String = savedStateHandle["classId"]
        ?: error("Missing class id")
    private val userId = authService.getCurrentUid()
    private val _classDetails = MutableStateFlow<ClassDetails?>(null)
    val classDetails = _classDetails.asStateFlow()
    private val _allClassSessions = MutableStateFlow<List<ClassSession>>(emptyList())
    private val _selectedDate = MutableStateFlow(
        startOfDay(System.currentTimeMillis())
    )
    private val _filteredClassSessions = MutableStateFlow<List<ClassSession>>(emptyList())
    val filteredClassSessions = _filteredClassSessions.asStateFlow()

    private val _bookedCountMap = MutableStateFlow<Map<String, Int>>(emptyMap())
    val bookedCountMap = _bookedCountMap.asStateFlow()

    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    init {
        getClassDetailsById(classId)
        getAllSessionsByClassId(classId)
    }
    fun getClassDetailsById(id: String) {
        viewModelScope.launch {
            LoadingManager.show()
            val result = withContext(Dispatchers.IO) {
                repo.getClassDetailsById(id)
            }
            result?.let {
                _classDetails.value = it
            }
            LoadingManager.hide()
        }
    }
    fun getAllSessionsByClassId(classId: String) {
        viewModelScope.launch {
            LoadingManager.show()
            val sessions = withContext(Dispatchers.IO) {
                repo.getActiveSessionsByClassId(classId)
            }
            _allClassSessions.value = sessions
            filterClassSessionsByDate(_selectedDate.value)
            LoadingManager.hide()
        }
    }
    fun filterClassSessionsByDate(dateMillis: Long) {
        val date = startOfDay(dateMillis)
        _selectedDate.value = date
        _filteredClassSessions.value = _allClassSessions.value.filter {
            it.date == date
        }
    }
    fun addBooking(sessionId: String, date: Long, startTime: Int, endTime: Int) {
        val booking = Booking(
             userId = userId ?: return,
             facilityId = null,
             sessionId = sessionId,
             date = date,
             startTime = startTime,
             endTime = endTime,
        )
        viewModelScope.launch {
            repo.addBooking(booking)
            getClassSessionBookingsBySessionId(sessionId, date)
//            _success.emit(Unit)
        }
    }
    fun getClassSessionBookingsBySessionId(sessionId: String, date: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = repo.getClassSessionBookingsBySessionId(
                sessionId = sessionId,
                date = date
            ).size
            _bookedCountMap.update {
                it + (sessionId to count)
            }
        }
    }
    fun refresh(sessions: List<ClassSession>) {
        sessions.forEach {
            getClassSessionBookingsBySessionId(
                it.id,
                it.date
            )
        }
    }
    fun canBook(
        session: ClassSession,
        bookedCount: Int
    ): Boolean {
        val today = startOfDay(System.currentTimeMillis())
        val nowMinutes = LocalTime.now().hour * 60 + LocalTime.now().minute

        return session.capacity != bookedCount &&
                (session.date > today ||
                        (session.date == today && session.startTime > nowMinutes)
                        )
    }
}