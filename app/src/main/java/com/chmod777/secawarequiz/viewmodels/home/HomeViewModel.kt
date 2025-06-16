package com.chmod777.secawarequiz.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.annotation.StringRes
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

data class HomeTestCardUIData(
    val id: String,
    @StringRes val titleResId: Int,
    val iconName: String,
    val accentColorName: String,
    @StringRes val contentDescResId: Int,
    val route: String
)

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String? = null,
    val testCards: List<HomeTestCardUIData> = emptyList(),
    val error: String? = null,
    val logoutSuccess: Boolean = false
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(100)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "SecAware User",
                testCards = listOf(
                    HomeTestCardUIData("phishing", R.string.home_card_title_phishing, "Security", "CardAccentPhishing", R.string.home_card_icon_desc_phishing, Screen.Minigame1.route),
                    HomeTestCardUIData("passwords", R.string.home_card_title_passwords, "Lock", "CardAccentPasswords", R.string.home_card_icon_desc_passwords, Screen.FakeLoginGame.route),
                    HomeTestCardUIData("literacy", R.string.home_card_title_literacy, "School", "CardAccentLiteracy", R.string.home_card_icon_desc_literacy, Screen.Quiz.createRoute(1)),
                    HomeTestCardUIData("datasec", R.string.home_card_title_data_security, "Dataset", "CardAccentDataSecurity", R.string.home_card_icon_desc_data_security, Screen.Quiz.createRoute(2))
                )
            )
        }
    }

    fun onLogoutClicked() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(500)
            _uiState.value = _uiState.value.copy(isLoading = false, logoutSuccess = true)
        }
    }

    fun onLogoutNavigationConsumed() {
        _uiState.value = _uiState.value.copy(logoutSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
