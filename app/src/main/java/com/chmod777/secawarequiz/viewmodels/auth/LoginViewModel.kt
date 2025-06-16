package com.chmod777.secawarequiz.viewmodels.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import android.content.Context
import android.content.Intent
import com.chmod777.secawarequiz.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val isPasswordResetLoading: Boolean = false,
    val passwordResetError: String? = null,
    val passwordResetSuccessMessage: String? = null
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

    fun sendPasswordResetEmail(context: Context, email: String) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordResetError = context.getString(R.string.error_email_password_empty))
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(passwordResetError = context.getString(R.string.error_invalid_email_format))
            return
        }

        _uiState.value = _uiState.value.copy(isPasswordResetLoading = true, passwordResetError = null, passwordResetSuccessMessage = null)

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isPasswordResetLoading = false,
                        passwordResetSuccessMessage = context.getString(R.string.password_reset_success_message)
                    )
                } else {
                    val errorMessage = task.exception?.message ?: context.getString(R.string.password_reset_error_message)
                    _uiState.value = _uiState.value.copy(
                        isPasswordResetLoading = false,
                        passwordResetError = errorMessage
                    )
                }
            }
    }

    fun clearPasswordResetMessages() {
        _uiState.value = _uiState.value.copy(passwordResetError = null, passwordResetSuccessMessage = null, isPasswordResetLoading = false)
    }

    fun setGoogleSignInError(errorMessage: String) {
        _uiState.value = _uiState.value.copy(error = errorMessage, isLoading = false, loginSuccess = false)
    }

    fun getGoogleSignInIntent(context: Context): Intent? {
        return try {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInClient.signOut()
            googleSignInClient.signInIntent
        } catch (e: Exception) {
            setGoogleSignInError("Error preparing Google Sign In: ${e.localizedMessage}")
            null
        }
    }

    fun firebaseAuthWithGoogle(context: Context, idToken: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(isLoading = false, loginSuccess = true, error = null)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = task.exception?.message ?: "Google Sign-In failed",
                        loginSuccess = false
                    )
                }
            }
    }
}
