package com.example.mob21project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.data.repo.ActivitiesRepo
import com.example.mob21project.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: ActivitiesRepo
): ViewModel() {
    val authUser = authService.authUser

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin = _isAdmin.asStateFlow()

    fun signOut() {
        authService.signOut()
        _isAdmin.value = false
    }

    fun checkIsAdmin() {
        viewModelScope.launch {
            val uid = authService.getCurrentUid() ?: return@launch
            _isAdmin.value = repo.isAdmin(uid)
        }
    }
}