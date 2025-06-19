package com.chmod777.secawarequiz.viewmodels.minigames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.data.GameItem
import com.chmod777.secawarequiz.data.GameItemDao
import com.chmod777.secawarequiz.data.ReviewDataHolder // Added import
import com.chmod777.secawarequiz.data.model.AnsweredGameItemDetails // Added import
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

    private val _answeredGameItemDetailsList = mutableListOf<AnsweredGameItemDetails>() // Added list

    init {
        loadGameItems()
    }

    fun loadGameItems() {
        _answeredGameItemDetailsList.clear() // Clear list when new game starts
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
        val selectedIndex = _selectedOptionIndex.value
        if (current != null && selectedIndex != null) {
            val wasCorrect = selectedIndex == current.correctOptionIndex
            if (wasCorrect) {
                _score.value += 1
            }
            // Store answered game item details
            val details = AnsweredGameItemDetails(
                gameItem = current,
                userAnswerIndex = selectedIndex,
                wasCorrect = wasCorrect
            )
            _answeredGameItemDetailsList.add(details)
        }
        _answerSubmitted.value = true
    }

    fun nextItem() {
        if (_currentItemIndex.value < _gameItems.value.size - 1) {
            _currentItemIndex.value++
            _currentItem.value = _gameItems.value[_currentItemIndex.value]
            resetCurrentItemState()
        } else {
            ReviewDataHolder.answeredGameItems = ArrayList(_answeredGameItemDetailsList) // Store for review
            _currentItem.value = null
            _gameItems.value = emptyList() // Also clear all game items list
            _currentItemIndex.value = 0 // Reset current item index
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
