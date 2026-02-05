package com.example.mob21project.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.LoadingManager
import com.example.mob21project.data.model.SearchResultUiModel
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: ActivitiesRepo
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _results = MutableStateFlow<List<SearchResultUiModel>>(emptyList())
    val results = _results.asStateFlow()

    fun onQueryChange(query: String) {
        _query.value = query
        search(query)
    }

    private fun search(query: String) {
        if (query.isBlank()) {
            _results.value = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val classes = repo.getAllClassDetails()
                .filter { it.title.contains(query, ignoreCase = true)}
                .map {
                    SearchResultUiModel.Class(
                        activityId = it.activityId,
                        classId = it.id,
                        title = it.title,
                        description = it.description,
                        imageUrl = it.imageUrl
                    )
                }
            val facilities = repo.getAllFacilityDetails()
                .filter { it.title.contains(query, ignoreCase = true) }
                .map {
                    SearchResultUiModel.Facility(
                        activityId = it.activityId,
                        facilityId = it.id,
                        title = it.title,
                        description = it.description,
                        imageUrl = it.imageUrl
                    )
                }
            _results.value = classes + facilities
        }
    }
}