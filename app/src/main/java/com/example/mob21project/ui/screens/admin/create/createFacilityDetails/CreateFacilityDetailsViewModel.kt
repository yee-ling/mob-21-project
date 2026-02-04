package com.example.mob21project.ui.screens.admin.create.createFacilityDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.SnackbarController
import com.example.mob21project.core.utils.SnackbarEvent
import com.example.mob21project.data.model.FacilityDetails
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateFacilityDetailsViewModel @Inject constructor(
    private val repo: ActivitiesRepo,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val activityId = savedStateHandle.get<String>("activityId")!!
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    fun createFacilityDetails(
        title: String,
        description: String,
        openingTime: Long,
        closingTime: Long,
        imageUrl: String?
    ) {
        val validationMsg = validate(
            title,
            description,
            openingTime,
            closingTime)
        if(validationMsg != null) {
            viewModelScope.launch {
                _error.emit(validationMsg)
                SnackbarController.sendEvent(SnackbarEvent(validationMsg))
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val facilityDetails = FacilityDetails(
                activityId = activityId,
                title = title,
                description = description,
                openingTime = openingTime,
                closingTime = closingTime,
                imageUrl = imageUrl
            )
            repo.addFacilityDetails(facilityDetails)
            _success.emit(Unit)
        }
    }
    fun validate(
        title: String,
        description: String,
        openingTime:Long,
        closingTime: Long
    ): String? {
        return try {
            require(title.isNotBlank()) { "Title is required" }
            require(description.isNotBlank()) { "Description is required" }
            require(openingTime > 0L ) { "Opening time is required" }
            require(closingTime > 0L ) { "Closing time is required" }
            require(openingTime < closingTime) {
                "Opening time must be before closing time"
            }
            null
        } catch (e: Exception) {
            e.message ?: "Invalid input"
        }
    }
}