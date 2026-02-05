package com.example.mob21project.ui.screens.admin.create.createActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.SnackbarController
import com.example.mob21project.core.utils.SnackbarEvent
import com.example.mob21project.data.model.Activity
import com.example.mob21project.data.model.ActivityType
import com.example.mob21project.data.model.createdEvent.CreatedActivityEvent
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateActivityViewModel @Inject constructor(
    private val repo: ActivitiesRepo
): ViewModel() {
    val activityTypes = ActivityType.entries.toList()
    private val _selectedType = MutableStateFlow(ActivityType.CLASS)
    val selectedType = _selectedType.asStateFlow()
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    private val _navigateEvent = MutableSharedFlow<CreatedActivityEvent>()
    val navigateEvent = _navigateEvent.asSharedFlow()
    fun onTypeSelected(type: ActivityType) {
        _selectedType.value = type
    }
    fun createActivity(name: String) {
        val validationMsg = validate(name)
        if(validationMsg != null) {
            viewModelScope.launch {
                _error.emit(validationMsg)
                SnackbarController.sendEvent(SnackbarEvent(validationMsg))
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val activity = Activity(
                name = name,
                type = _selectedType.value
            )
            val activityId = repo.addActivity(activity)
            _navigateEvent.emit(
                CreatedActivityEvent(
                    activityId = activityId,
                    type = _selectedType.value
                )
            )
        }
    }
    fun validate(name: String): String? {
        return try {
            require(name.isNotBlank()) { "Name is required" }
            null
        } catch (e: Exception) {
            e.message ?: "Invalid input"
        }
    }
}