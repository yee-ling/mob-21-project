package com.example.mob21project.data.model

sealed class SearchResultUiModel {
    data class Class(
        val activityId: String,
        val classId: String,
        val title: String,
        val description: String,
        val imageUrl: String? = null
    ) : SearchResultUiModel()

    data class Facility(
        val activityId: String,
        val facilityId: String,
        val title: String,
        val description: String,
        val imageUrl: String? = null
    ) : SearchResultUiModel()
}