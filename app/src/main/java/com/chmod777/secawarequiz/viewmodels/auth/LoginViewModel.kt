package com.chmod777.secawarequiz.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onLoginClicked(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Email and password cannot be empty.")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            delay(1000)
            if (email == "test@example.com" && pass == "password") {
                 _uiState.value = _uiState.value.copy(isLoading = false, loginSuccess = true)
            } else {
                 _uiState.value = _uiState.value.copy(isLoading = false, error = "Invalid credentials (dummy check).")
            }
        }
    }

    fun onLoginSuccessNavigationConsumed() {
        _uiState.value = _uiState.value.copy(loginSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
