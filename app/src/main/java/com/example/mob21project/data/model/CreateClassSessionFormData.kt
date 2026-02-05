package com.example.mob21project.data.model

data class CreateClassSessionFormData(
    val date: Long,
    val durationMinutes: Double,
    val startTime: Int,
    val endTime: Int,
    val capacity: Int
) {
    fun isValid(): Boolean =
        startTime < endTime && durationMinutes > 0 && capacity > 0
}
