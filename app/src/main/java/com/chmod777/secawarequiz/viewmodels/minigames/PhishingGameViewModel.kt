package com.chmod777.secawarequiz.viewmodels.minigames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.data.GameItem
import com.chmod777.secawarequiz.data.GameItemDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PhishingGameViewModel(private val gameItemDao: GameItemDao) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _gameItems = MutableStateFlow<List<GameItem>>(emptyList())
    val gameItems: StateFlow<List<GameItem>> = _gameItems.asStateFlow()

    private val _currentItemIndex = MutableStateFlow(0)
    val currentItemIndex: StateFlow<Int> = _currentItemIndex.asStateFlow()

    private val _currentItem = MutableStateFlow<GameItem?>(null)
    val currentItem: StateFlow<GameItem?> = _currentItem.asStateFlow()

    private val _selectedOptionIndex = MutableStateFlow<Int?>(null)
    val selectedOptionIndex: StateFlow<Int?> = _selectedOptionIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _answerSubmitted = MutableStateFlow(false)
    val answerSubmitted: StateFlow<Boolean> = _answerSubmitted.asStateFlow()

    private val _showResults = MutableStateFlow(false)
    val showResults: StateFlow<Boolean> = _showResults.asStateFlow()

    init {
        loadGameItems()
    }

    fun loadGameItems() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            gameItemDao.getAllGameItems()
                .catch { e ->
                    _error.value = "Ошибка загрузки игровых данных: ${e.message}"
                    _isLoading.value = false
                }
                .collect { items ->
                    _gameItems.value = items.shuffled()
                    if (_gameItems.value.isNotEmpty()) {
                        _currentItemIndex.value = 0
                        _currentItem.value = _gameItems.value[0]
                        _showResults.value = false
                        _score.value = 0
                    } else {
                        _currentItem.value = null
                        _error.value = "Нет доступных игровых данных."
                    }
                    _isLoading.value = false
                    resetCurrentItemState()
                }
        }
    }

    fun selectOption(index: Int) {
        if (!_answerSubmitted.value) {
            _selectedOptionIndex.value = index
        }
    }

    fun submitAnswer() {
        if (_selectedOptionIndex.value == null) {
            _error.value = "Пожалуйста, выберите вариант ответа."
            return
        }
        _error.value = null

        val current = _currentItem.value
        if (current != null && _selectedOptionIndex.value == current.correctOptionIndex) {
            _score.value += 1
        }
        _answerSubmitted.value = true
    }

    fun nextItem() {
        if (_currentItemIndex.value < _gameItems.value.size - 1) {
            _currentItemIndex.value++
            _currentItem.value = _gameItems.value[_currentItemIndex.value]
            resetCurrentItemState()
        } else {
            _showResults.value = true
        }
    }

    private fun resetCurrentItemState() {
        _selectedOptionIndex.value = null
        _answerSubmitted.value = false
        _error.value = null
    }

    fun restartGame() {
        _isLoading.value = true
        val shuffledItems = _gameItems.value.shuffled()
        _gameItems.value = shuffledItems

        if (shuffledItems.isNotEmpty()) {
            _currentItemIndex.value = 0
            _currentItem.value = shuffledItems[0]
            _score.value = 0
            _showResults.value = false
            resetCurrentItemState()
        } else {

            _currentItem.value = null
            _error.value = "Нет данных для перезапуска игры."
        }
        _isLoading.value = false
    }

    fun onResultsNavigated() {
        _showResults.value = false
    }

    companion object {
        fun provideFactory(gameItemDao: GameItemDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PhishingGameViewModel::class.java)) {
                        return PhishingGameViewModel(gameItemDao) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
