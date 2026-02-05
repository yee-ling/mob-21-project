package com.example.mob21project.ui.screens.admin.create.createClassDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.SnackbarController
import com.example.mob21project.core.utils.SnackbarEvent
import com.example.mob21project.data.model.ClassDetails
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateClassDetailsViewModel @Inject constructor(
    private val repo: ActivitiesRepo,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val activityId = savedStateHandle.get<String>("activityId")!!
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    fun createClassDetails(
        title: String,
        description: String,
        durationMinutes: Double,
        capacity: Int,
        imageUrl: String?
    ) {
        val validationMsg = validate(title,description)
        if(validationMsg != null) {
            viewModelScope.launch {
                _error.emit(validationMsg)
                SnackbarController.sendEvent(SnackbarEvent(validationMsg))
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val classDetails = ClassDetails(
                 activityId = activityId,
                 title = title,
                 description = description,
                 durationMinutes = durationMinutes,
                 capacity = capacity,
                imageUrl = imageUrl
            )
            repo.addClassDetails(classDetails)
            _success.emit(Unit)
        }
    }
    fun validate(title: String, description: String): String? {
        return try {
            require(title.isNotBlank()) { "Title is required" }
            require(description.isNotBlank()) { "Description is required" }
            null
        } catch (e: Exception) {
            e.message ?: "Invalid input"
        }
    }
}