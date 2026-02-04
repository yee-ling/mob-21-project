package com.example.mob21project.data.model

data class FacilityDetails(
    val activityId: String = "",
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val openingTime: Long = System.currentTimeMillis(),
    val closingTime: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)