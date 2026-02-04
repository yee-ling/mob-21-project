package com.example.mob21project.ui.screens.admin.manage.manageBookings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.data.model.Booking
import com.example.mob21project.data.model.User
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ManageBookingsViewModel @Inject constructor(
    private val repo: ActivitiesRepo,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val sessionId: String = savedStateHandle["sessionId"]
        ?: error("Missing session id")

    private val _bookings = MutableStateFlow<List<ManageBookingUiModel>>(emptyList())
    val bookings = _bookings.asStateFlow()

    init {
        getBookingsForClassSession()
    }

    fun getBookingsForClassSession() {
        viewModelScope.launch(Dispatchers.IO) {
            val bookings = repo.getBookingsForClassSession(sessionId)

            _bookings.value = bookings.map { booking ->
                ManageBookingUiModel(
                    booking = booking,
                    user = repo.getUserById(booking.userId)
                )
            }
        }
    }
}

data class ManageBookingUiModel(
    val booking: Booking,
    val user: User?
)