package com.example.mob21project.data.model

data class ClassSession(
    val id: String = "",
    val activityId: String = "",
    val date: Long = System.currentTimeMillis(),
    val durationMinutes: Double = 0.0,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val capacity: Int = 0,
    val status: ClassSessionStatus = ClassSessionStatus.ACTIVE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ClassSessionStatus {
    ACTIVE, CANCELLED
}