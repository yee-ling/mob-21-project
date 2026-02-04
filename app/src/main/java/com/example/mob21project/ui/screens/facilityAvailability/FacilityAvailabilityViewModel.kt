package com.example.mob21project.ui.screens.facilityAvailability

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.LoadingManager
import com.example.mob21project.core.utils.SnackbarController
import com.example.mob21project.core.utils.SnackbarEvent
import com.example.mob21project.data.model.Booking
import com.example.mob21project.data.model.FacilityDetails
import com.example.mob21project.data.repo.ActivitiesRepo
import com.example.mob21project.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.String

@HiltViewModel
class FacilityAvailabilityViewModel @Inject constructor(
    private val repo: ActivitiesRepo,
    private val savedStateHandle: SavedStateHandle,
    private val authService: AuthService
): ViewModel() {
    private val userId = authService.getCurrentUid()
    private val id: String = savedStateHandle["id"]
        ?: error("Missing facility id")
    private val _facilityDetails = MutableStateFlow<FacilityDetails?>(null)
    val facilityDetails = _facilityDetails.asStateFlow()
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()
    private val _timeSlots = MutableStateFlow<List<Int>>(emptyList())
    val timeSlots = _timeSlots.asStateFlow()
    private val _selectedSlots = MutableStateFlow<List<Int>>(emptyList())
    val selectedSlots = _selectedSlots.asStateFlow()
    private var selectionStart: Int? = null
    private var selectionEnd: Int? = null
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings = _bookings.asStateFlow()
    private val _disabledSlots = MutableStateFlow<Set<Int>>(emptySet())
    val disabledSlots = _disabledSlots.asStateFlow()

    init {
        getFacilityDetailsById(id)
    }

    fun getFacilityDetailsById(id: String) {
        viewModelScope.launch {
            try {
                LoadingManager.show()
                val result = withContext(Dispatchers.IO) {
                    repo.getFacilityDetailsById(id)
                }
                result?.let {
                    _facilityDetails.value = it

                    val slots = generateTimeSlots(
                        openingTime = it.openingTime.toInt(),
                        closingTime = it.closingTime.toInt(),
                        intervalMinutes = 30
                    )
                    _timeSlots.value = slots
                }
            } finally {
                LoadingManager.hide()
            }

        }
    }
    fun generateTimeSlots(
        openingTime: Int,
        closingTime: Int,
        intervalMinutes: Int = 30
    ): List<Int> {
        val slots = mutableListOf<Int>()
        var current = openingTime
        while (current + intervalMinutes <= closingTime) {
            slots.add(current)
            current += intervalMinutes
        }
        return slots
    }

    fun onSlotClick(slot: Int) {
        if (selectionStart == null) {
            // First click -> set start
            selectionStart = slot
//            selectionEnd = slot
            selectionEnd = null
            _selectedSlots.value = listOf(slot)
        } else {
            // Second click -> set end
            selectionEnd = slot

            val start = minOf(selectionStart!!, selectionEnd!!)
            val end = maxOf(selectionStart!!, selectionEnd!!)

            _selectedSlots.value = highlightTimeSlots(start, end)
        }
        if(selectionEnd == selectionStart) {
            clearSelection()
        }
    }
    fun clearSelection() {
        selectionStart = null
        selectionEnd = null
        _selectedSlots.value = emptyList()
    }

    fun highlightTimeSlots(
        start: Int,
        end: Int,
        intervalMinutes: Int = 30
    ): List<Int> {
        val highlightedSlots = mutableListOf<Int>()
        var current = start
        while (current <= end) {
            highlightedSlots.add(current)
            current += intervalMinutes
        }
        return highlightedSlots
    }
    fun addBooking(date: Long, startTime: Int, endTIme: Int) {
        val bookings = _bookings.value
        val validationMsg = validate(startTime, endTIme, bookings)
        if(validationMsg != null) {
            viewModelScope.launch {
                _error.emit(validationMsg)
                SnackbarController.sendEvent(SnackbarEvent(validationMsg))
            }
            return
        }
        val booking = Booking(
            userId = userId ?: return,
            facilityId = id,
            sessionId = null,
            date = date,
            startTime = startTime,
            endTime = endTIme,
        )
        viewModelScope.launch(Dispatchers.IO) {
            repo.addBooking(booking)
            clearSelection()
            _success.emit(Unit)
        }
    }

    fun validate(startTime: Int, endTIme: Int, bookings: List<Booking>): String? {
        return try {
            require(startTime != endTIme) { "Invalid booking duration" }
            val overlap = bookings.any { booking ->
                startTime < booking.endTime && endTIme > booking.startTime
            }
            require(!overlap) { "Selected time overlaps with existing booking" }
            null
        } catch (e: Exception) {
            e.message ?: "Invalid input"
        }
    }
    fun getAllBookingsByFacilityId(date: Long) {
        viewModelScope.launch {
            try {
                LoadingManager.show()

                val bookings = withContext(Dispatchers.IO) {
                    repo.getFacilityBookingsByFacilityId(id, date)
                }

                _bookings.value = bookings
                _disabledSlots.value = disableTimeSlots(bookings)
            } finally {
                LoadingManager.hide()
            }
        }
    }
    fun disableTimeSlots(bookings: List<Booking>): Set<Int> {
        val slots = _timeSlots.value
        return bookings.flatMap { booking ->
            slots.filter { slot ->
                slot >= booking.startTime && slot < booking.endTime
            }
        }.toSet()
    }
}