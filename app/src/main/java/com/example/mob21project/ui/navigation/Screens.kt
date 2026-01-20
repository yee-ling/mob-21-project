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
    @Serializable object ClassSessions: Screen()
    @Serializable object FacilityAvailability: Screen()
}