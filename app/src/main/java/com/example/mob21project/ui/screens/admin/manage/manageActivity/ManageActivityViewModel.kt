package com.example.mob21project.ui.screens.admin.manage.manageActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.data.model.ClassDetails
import com.example.mob21project.data.model.FacilityDetails
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ManageActivityViewModel @Inject constructor(
    val repo: ActivitiesRepo
): ViewModel() {
    private val _allClassDetails = MutableStateFlow<List<ClassDetails>>(emptyList())
    val allClassDetails = _allClassDetails.asStateFlow()
    private val _allFacilityDetails = MutableStateFlow<List<FacilityDetails>>(emptyList())
    val allFacilityDetails = _allFacilityDetails.asStateFlow()
    fun getAllClassDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            _allClassDetails.value = repo.getAllClassDetails()
        }
    }
    fun getAllFacilityDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            _allFacilityDetails.value = repo.getAllFacilityDetails()
        }
    }
}