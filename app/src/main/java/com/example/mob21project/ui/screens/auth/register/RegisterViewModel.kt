package com.example.mob21project.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob21project.core.utils.SnackbarController
import com.example.mob21project.core.utils.SnackbarEvent
import com.example.mob21project.data.model.User
import com.example.mob21project.data.repo.ActivitiesRepo
import com.example.mob21project.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: ActivitiesRepo
): ViewModel() {
    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    fun signUpWithEmail(email: String, password: String, fullName: String) {
        val validationMsg = validate(email, password, fullName)
        if (validationMsg != null) {
            viewModelScope.launch {
                _error.emit(validationMsg)
                SnackbarController.sendEvent(SnackbarEvent(validationMsg))
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = authService.signUpWithEmail(email, password) ?: return@launch
                val uid = user.uid
                val userExist = repo.getUserById(uid)
                if(userExist == null) {
                    val newUser = User(
                        id = uid,
                        email = email,
                        fullName = fullName
                    )
                    repo.addUser(newUser)
                }
                _success.emit(Unit)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Sign up failed"
                _error.emit(errorMsg)
                withContext(Dispatchers.Main) {
                    SnackbarController.sendEvent(SnackbarEvent(errorMsg))
                }
            }
        }
    }
    fun validate(email: String, password: String, fullName: String): String? {
        return try {
            require(email.isNotBlank()) { "Email is required" }
            require(password.isNotBlank()) { "Password is required" }
            require(fullName.isNotBlank()) { "Name is required" }
            null
        } catch (e: Exception) {
            e.message ?: "Invalid input"
        }
    }
}