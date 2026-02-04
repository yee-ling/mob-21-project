package com.example.mob21project.data.model

data class ClassSession(
    val id: String = "",
    val activityId: String = "",
    val classId: String = "",
    val date: Long = System.currentTimeMillis(),
    val durationMinutes: Double = 0.0,
    val startTime: Int = 0,
    val endTime: Int = 0,
    val capacity: Int = 0,
    val status: ClassSessionStatus = ClassSessionStatus.ACTIVE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val cancelledAt: Long? = null
)

enum class ClassSessionStatus {
    ACTIVE, CANCELLED
}