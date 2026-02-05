package com.example.mob21project.ui.screens.facilityDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.LoadingManager
import com.example.mob21project.data.model.FacilityDetails
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class FacilityDetailsViewModel @Inject constructor(
    val repo: ActivitiesRepo
): ViewModel() {
    private val _allFacilityDetails = MutableStateFlow<List<FacilityDetails>>(emptyList())
    val allFacilityDetails = _allFacilityDetails.asStateFlow()
    fun getAllFacilityDetails() {
        viewModelScope.launch {
            LoadingManager.show()
            val result = withContext(Dispatchers.IO) {
                repo.getAllFacilityDetails()
            }
            _allFacilityDetails.value = result
            LoadingManager.hide()
        }
    }
}