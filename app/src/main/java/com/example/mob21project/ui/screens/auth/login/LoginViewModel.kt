package com.example.mob21project.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.SnackbarController
import com.example.mob21project.core.utils.SnackbarEvent
import com.example.mob21project.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel() {
    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    fun signInWithEmail(email: String, password: String) {
        val validationMsg = validate(email, password)
        if (validationMsg != null) {
            viewModelScope.launch {
                _error.emit(validationMsg)
                SnackbarController.sendEvent(SnackbarEvent(validationMsg))
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authService.signInWithEmail(email, password)
                _success.emit(Unit)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Sign in failed"
                _error.emit(errorMsg)
                withContext(Dispatchers.Main) {
                    SnackbarController.sendEvent(SnackbarEvent(errorMsg))
                }
            }
        }
    }
    fun validate(email: String, password: String): String? {
        return try {
            require(email.isNotBlank()) { "Email is required" }
            require(password.isNotBlank()) { "Password is required" }
            null
        } catch (e: Exception) {
            e.message ?: "Invalid input"
        }
    }
}