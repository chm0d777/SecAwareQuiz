package com.chmod777.secawarequiz.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class RegistrationUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registrationSuccess: Boolean = false
)

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onRegisterClicked(email: String, pass: String, confirmPass: String) {
        if (email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "All fields are required.")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(error = "Invalid email format.")
            return
        }
        if (pass.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Password must be at least 6 characters.")
            return
        }
        if (pass != confirmPass) {
            _uiState.value = _uiState.value.copy(error = "Passwords do not match.")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            delay(1500)
            _uiState.value = _uiState.value.copy(isLoading = false, registrationSuccess = true, error = null)
        }
    }

    fun onRegistrationSuccessNavigationConsumed() {
        _uiState.value = _uiState.value.copy(registrationSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
