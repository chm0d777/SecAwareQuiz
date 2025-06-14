package com.chmod777.secawarequiz.viewmodels.minigames

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Minigame1UiState(
    val isLoading: Boolean = false,
    val score: Int = 0,
    val lives: Int = 3,
    val currentChallenge: String? = null,
    val isGameOver: Boolean = false,
    val error: String? = null
)

class Minigame1ViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(Minigame1UiState())
    val uiState: StateFlow<Minigame1UiState> = _uiState.asStateFlow()

    init {
        loadGame()
    }

    private fun loadGame() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        GlobalScope.launch {
            delay(500)
            _uiState.value = Minigame1UiState(isLoading = false, currentChallenge = "Миниигра 1 готова", lives = 3, score = 0)
        }
    }

    fun restartGame() {
        loadGame()
    }
}
