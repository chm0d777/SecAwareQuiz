package com.chmod777.secawarequiz.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.ui.screens.home.HomeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String? = null,
    val quizList: List<HomeItem> = emptyList(),
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
            delay(500)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                userName = "SecAware User",
                quizList = listOf(
                    HomeItem("q101", "Фишинг", "Научись отличать фишинговые письма"),
                    HomeItem("q102", "Сильные пароли", "Создавать и хранить сильные пароли"),
                    HomeItem("m201", "Найди паль (миниигра)", "Найди фейковый сайт за определённое время"),
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
