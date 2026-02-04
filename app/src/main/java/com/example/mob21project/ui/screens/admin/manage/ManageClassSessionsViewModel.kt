package com.example.mob21project.ui.screens.admin.manage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.SnackbarController
import com.example.mob21project.core.utils.SnackbarEvent
import com.example.mob21project.data.model.ClassDetails
import com.example.mob21project.data.model.ClassSession
import com.example.mob21project.data.model.CreateClassSessionFormData
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
class ManageClassSessionsViewModel @Inject constructor(
    private val repo: ActivitiesRepo,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val classId = savedStateHandle.get<String>("classId")!!
    private var activityId: String? = null
    private val _classDetails = MutableStateFlow<ClassDetails?>(null)
    val classDetails = _classDetails.asStateFlow()
    private val _allClassSessions = MutableStateFlow<List<ClassSession>>(emptyList())
    val allClassSessions = _allClassSessions.asStateFlow()
    private val _cancelledClassSessions = MutableStateFlow<List<ClassSession>>(emptyList())
    val cancelledClassSessions = _cancelledClassSessions.asStateFlow()
    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    init {
        getClassDetailsById(classId)
        getAllActiveSessionsByClassId(classId)
        getAllCancelledSessionsByClassId(classId)
    }

    fun getClassDetailsById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getClassDetailsById(id)?.let {
                activityId = it.activityId
                _classDetails.value = it
            }
        }
    }
    fun getAllActiveSessionsByClassId(classId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _allClassSessions.value = repo.getActiveSessionsByClassId(classId)
        }
    }
    fun getAllCancelledSessionsByClassId(classId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _cancelledClassSessions.value = repo.getCancelledSessionsByClassId(classId)
        }
    }
    fun addClassSession(formData: CreateClassSessionFormData) {
        if (!formData.isValid()) return
        viewModelScope.launch(Dispatchers.IO) {
            val classSession = ClassSession(
                 activityId = activityId ?: return@launch,
                 classId = classId,
                 date = formData.date,
                 durationMinutes = formData.durationMinutes,
                 startTime = formData.startTime,
                 endTime = formData.endTime,
                 capacity = formData.capacity,
            )
            repo.addClassSession(classSession)
            _success.emit(Unit)
        }
    }
    fun cancelClassSession(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.cancelSession(id)
            SnackbarController.sendEvent(
                SnackbarEvent("Session cancelled")
            )
            refresh()
        }
    }
    fun refresh() {
        getAllActiveSessionsByClassId(classId)
        getAllCancelledSessionsByClassId(classId)
    }
}