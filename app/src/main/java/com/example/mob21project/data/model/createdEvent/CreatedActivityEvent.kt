package com.example.mob21project.data.model.createdEvent

import com.example.mob21project.data.model.ActivityType

data class CreatedActivityEvent(
    val activityId: String,
    val type: ActivityType
)
