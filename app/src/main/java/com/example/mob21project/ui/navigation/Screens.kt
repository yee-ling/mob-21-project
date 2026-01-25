package com.example.mob21project.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable object Home: Screen()
    @Serializable object Login: Screen()
    @Serializable object Register: Screen()
    @Serializable object Search: Screen()
    @Serializable object ClassDetails: Screen()
    @Serializable object FacilityDetails: Screen()
    @Serializable object Bookings: Screen()
    @Serializable data class ClassSessions(val classId: String): Screen()
    @Serializable data class FacilityAvailability(val id: String): Screen()
    @Serializable object AdminDashboard: Screen()
    @Serializable object CreateActivity: Screen()
    @Serializable data class CreateClassDetails(val activityId: String): Screen()
    @Serializable data class CreateFacilityDetails(val activityId: String): Screen()
    @Serializable object ManageActivity: Screen()
    @Serializable data class ManageClassSessions(val classId: String): Screen()
}