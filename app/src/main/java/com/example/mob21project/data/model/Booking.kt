package com.example.mob21project.data.model

data class Booking(
    val id: String = "",
    val userId: String = "",
    val facilityId: String? = null,
    val sessionId: String? = null,
    val date: Long = System.currentTimeMillis(),
    val startTime: Int = 0,
    val endTime: Int = 0,
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val cancelledAt: Long? = null
) {
    fun isValid(): Boolean {
        return (facilityId != null && sessionId == null) ||
                (facilityId == null && sessionId != null)
    }
}

enum class BookingStatus {
    CONFIRMED, CANCELLED
}