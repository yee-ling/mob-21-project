package com.example.mob21project.data.model

data class Activity (
    val id: String = "",
    val name: String = "",
    val type: ActivityType = ActivityType.CLASS,
    val status: ActivityStatus = ActivityStatus.ACTIVE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ActivityType {
    CLASS, FACILITY
}
enum class ActivityStatus {
    ACTIVE, INACTIVE
}