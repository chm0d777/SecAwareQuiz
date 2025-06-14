package com.chmod777.secawarequiz.viewmodels.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import android.content.Context
import com.chmod777.secawarequiz.R

data class RegistrationUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registrationSuccess: Boolean = false
)

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun onRegisterClicked(context: Context, email: String, pass: String, confirmPass: String) {
        if (email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            _uiState.value = _uiState.value.copy(error = context.getString(R.string.error_all_fields_required))
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(error = context.getString(R.string.error_invalid_email_format))
            return
        }
        if (pass.length < 6) {
            _uiState.value = _uiState.value.copy(error = context.getString(R.string.error_password_length))
            return
        }
        if (pass != confirmPass) {
            _uiState.value = _uiState.value.copy(error = context.getString(R.string.error_passwords_do_not_match))
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                _uiState.value = _uiState.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(registrationSuccess = true, error = null)
                } else {
                    val exception = task.exception
                    val errorMessage = when (exception) {
                        is FirebaseAuthWeakPasswordException -> context.getString(R.string.error_weak_password)
                        is FirebaseAuthUserCollisionException -> context.getString(R.string.error_user_collision)
                        else -> exception?.message ?: context.getString(R.string.error_unknown_registration)
                    }
                    _uiState.value =
                        _uiState.value.copy(error = errorMessage, registrationSuccess = false)
                }
            }
    }

    fun onRegistrationSuccessNavigationConsumed() {
        _uiState.value = _uiState.value.copy(registrationSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
