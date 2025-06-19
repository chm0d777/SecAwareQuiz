package com.chmod777.secawarequiz.viewmodels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.chmod777.secawarequiz.data.ThemeRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel

data class ProfileUiState(
    val firebaseUser: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showThemeDialog: Boolean = false,
    val currentThemePreference: String = "",
    val showLanguageDialog: Boolean = false,
    val currentLanguagePreference: String = ""
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val appSettingsRepository = ThemeRepository(application.applicationContext)

    init {
        loadUserProfile()
        loadThemePreference()
        loadLanguagePreference()
    }

    private fun loadThemePreference() {
        _uiState.update { it.copy(currentThemePreference = appSettingsRepository.getThemePreference()) }
    }

    private fun loadLanguagePreference() {
        _uiState.update { it.copy(currentLanguagePreference = appSettingsRepository.getLanguagePreference()) }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update {
                it.copy(
                    firebaseUser = firebaseAuth.currentUser,
                    isLoading = false
                )
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _uiState.update { it.copy(firebaseUser = null) }
    }

    fun onThemeSettingsClicked() {
        _uiState.update { it.copy(showThemeDialog = true) }
    }

    fun onThemeDialogDismiss() {
        _uiState.update { it.copy(showThemeDialog = false) }
    }

    fun onThemeSelected(themePreference: String) {
        appSettingsRepository.saveThemePreference(themePreference)
        _uiState.update { it.copy(currentThemePreference = themePreference, showThemeDialog = false) }
    }

    fun onLanguageSettingsClicked() {
        _uiState.update { it.copy(showLanguageDialog = true) }
    }

    fun onLanguageDialogDismiss() {
        _uiState.update { it.copy(showLanguageDialog = false) }
    }

    fun onLanguageSelected(languageCode: String) {
        appSettingsRepository.saveLanguagePreference(languageCode)
        _uiState.update { it.copy(currentLanguagePreference = languageCode, showLanguageDialog = false) }
    }
}
