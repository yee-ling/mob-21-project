package com.example.mob21project.ui.screens.admin.manage.manageUsers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.data.model.User
import com.example.mob21project.data.repo.ActivitiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ManageUsersViewModel @Inject constructor(
    val repo: ActivitiesRepo
): ViewModel() {
    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers = _allUsers.asStateFlow()

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _allUsers.value = repo.getAllUsers()
        }
    }
}