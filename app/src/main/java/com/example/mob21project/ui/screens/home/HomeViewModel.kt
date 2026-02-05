package com.example.mob21project.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.LoadingManager
import com.example.mob21project.data.model.ClassDetails
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repo: ActivitiesRepo
): ViewModel() {
    private val _availableClasses = MutableStateFlow<List<ClassDetails>>(emptyList())
    val availableClasses = _availableClasses.asStateFlow()

    init {
        getAvailableClasses()
    }

    fun getAvailableClasses() {
        viewModelScope.launch {
            LoadingManager.show()
            val result = withContext(Dispatchers.IO) {
                repo.getAllClassDetails()
            }
            _availableClasses.value = result
            LoadingManager.hide()
        }
    }
}