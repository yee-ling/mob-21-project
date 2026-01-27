package com.example.mob21project.ui.screens.bookings

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.data.model.Booking
import com.example.mob21project.data.repo.ActivitiesRepo
import com.example.mob21project.service.AuthService
import com.example.mob21project.ui.utils.startOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val repo: ActivitiesRepo,
    private val authService: AuthService
): ViewModel() {
    private val userId = authService.getCurrentUid() ?: error("Missing user id")
    private val _upcomingBookings = MutableStateFlow<List<BookingUiModel>>(emptyList())
    val upcomingBookings = _upcomingBookings.asStateFlow()
    private val _cancelledBookings = MutableStateFlow<List<BookingUiModel>>(emptyList())
    val cancelledBookings = _cancelledBookings.asStateFlow()
    private val _historyBookings = MutableStateFlow<List<BookingUiModel>>(emptyList())
    val historyBookings = _historyBookings.asStateFlow()

    init {
        getConfirmedBookings()
        getCancelledBookings()
    }
    fun currentMinutesOfDay(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    }
    fun getConfirmedBookings() {
        viewModelScope.launch(Dispatchers.IO) {
            val startOfToday = startOfDay(System.currentTimeMillis())
            val currentTimeInMinutes = currentMinutesOfDay()

            val bookings = repo.getConfirmedBookingsByCurrentUser(userId)

            val uiModels = bookings.mapNotNull { booking ->
                when {
                    booking.facilityId != null -> {
                        val facilityDetails = repo.getFacilityDetailsById(booking.facilityId)
                        facilityDetails?.let {
                            BookingUiModel(
                                booking = booking,
                                title = it.title,
                                description = it.description,
                                type = BookingType.FACILITY
                            )
                        }
                    }
                    booking.sessionId != null -> {
                        val session = repo.getClassSessionById(booking.sessionId)

                        session?.let {
                            val classDetails = repo.getClassDetailsById(it.classId)
                            classDetails?.let { classDetails ->
                                BookingUiModel(
                                    booking = booking,
                                    title = classDetails.title,
                                    description = classDetails.description,
                                    type = BookingType.CLASS
                                )
                            }
                        }
                    }
                    else -> null
                }
            }
            val upcoming = uiModels.filter { ui ->
                val booking = ui.booking
                booking.date > startOfToday ||
                        (booking.date == startOfToday && booking.endTime > currentTimeInMinutes)

            }
            val history = uiModels.filter { ui ->
                val booking = ui.booking
                booking.date < startOfToday ||
                        (booking.date == startOfToday && booking.endTime <= currentTimeInMinutes)

            }
            _upcomingBookings.value = upcoming
            _historyBookings.value = history
        }
    }
    fun getCancelledBookings() {
        viewModelScope.launch(Dispatchers.IO) {
            val bookings = repo.getCancelledBookingsByCurrentUser(userId)

            val uiModels = bookings.mapNotNull { booking ->
                when {
                    booking.facilityId != null -> {
                        val facilityDetails = repo.getFacilityDetailsById(booking.facilityId)
                        facilityDetails?.let {
                            BookingUiModel(
                                booking = booking,
                                title = it.title,
                                description = it.description,
                                type = BookingType.FACILITY
                            )
                        }
                    }
                    booking.sessionId != null -> {
                        val session = repo.getClassSessionById(booking.sessionId)

                        session?.let {
                            val classDetails = repo.getClassDetailsById(it.classId)
                            classDetails?.let { classDetails ->
                                BookingUiModel(
                                    booking = booking,
                                    title = classDetails.title,
                                    description = classDetails.description,
                                    type = BookingType.CLASS
                                )
                            }
                        }
                    }
                    else -> null
                }
            }
            _cancelledBookings.value = uiModels
        }
    }
}
data class BookingUiModel(
    val booking: Booking,
    val title: String,
    val description: String?,
    val type: BookingType
)
enum class BookingType {
    CLASS, FACILITY
}