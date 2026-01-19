package com.example.mob21project.data.model

data class ClassDetails(
    val activityId: String = "",
    val title: String = "",
    val description: String = "",
    val durationMinutes: Double = 0.0,
    val capacity: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)