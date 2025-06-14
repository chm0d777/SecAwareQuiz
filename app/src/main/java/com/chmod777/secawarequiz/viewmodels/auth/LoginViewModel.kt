package com.chmod777.secawarequiz.viewmodels.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import android.content.Context
import com.chmod777.secawarequiz.R

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun onLoginClicked(context: Context, email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = _uiState.value.copy(error = context.getString(R.string.error_email_password_empty))
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(error = context.getString(R.string.error_invalid_email_format))
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                _uiState.value = _uiState.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(loginSuccess = true)
                } else {
                    val exception = task.exception
                    val errorMessage = when (exception) {
                        is FirebaseAuthInvalidUserException -> context.getString(R.string.error_no_account_found)
                        is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.error_incorrect_password)
                        else -> exception?.message ?: context.getString(R.string.error_unknown_login)
                    }
                    _uiState.value = _uiState.value.copy(error = errorMessage)
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
