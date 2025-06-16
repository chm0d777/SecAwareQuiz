package com.chmod777.secawarequiz.viewmodels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val firebaseUser: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentThemeValue: String = "Dark",
    val currentLanguageValue: String = "Russian"
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _uiState.value = _uiState.value.copy(
                firebaseUser = firebaseAuth.currentUser,
                isLoading = false
            )
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _uiState.value = _uiState.value.copy(firebaseUser = null)
    }
}
