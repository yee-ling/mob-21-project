package com.example.mob21project.data.model

data class Booking(
    val id: String = "",
    val userId: String = "",
    val activityId: String = "",
    val sessionId: String? = null,
    val date: Long = System.currentTimeMillis(),
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class BookingStatus {
    CONFIRMED, CANCELLED
}