package com.chmod777.secawarequiz.viewmodels.minigames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.data.FakeLoginGameItem
import com.chmod777.secawarequiz.data.FakeLoginGameItemDao
import com.chmod777.secawarequiz.data.ReviewDataHolder
import com.chmod777.secawarequiz.data.model.AnsweredFakeLoginItemDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FakeLoginViewModel(private val dao: FakeLoginGameItemDao) : ViewModel() {

    private val _gameItems = MutableStateFlow<List<FakeLoginGameItem>>(emptyList())
    val gameItems: StateFlow<List<FakeLoginGameItem>> = _gameItems.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentItemIndex = MutableStateFlow(0)


    private val _currentItem = MutableStateFlow<FakeLoginGameItem?>(null)
    val currentItem: StateFlow<FakeLoginGameItem?> = _currentItem.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _userAnswered = MutableStateFlow(false)
    val userAnswered: StateFlow<Boolean> = _userAnswered.asStateFlow()

    private val _isCorrect = MutableStateFlow<Boolean?>(null)
    val isCorrect: StateFlow<Boolean?> = _isCorrect.asStateFlow()

    private val _showResults = MutableStateFlow(false)
    val showResults: StateFlow<Boolean> = _showResults.asStateFlow()

    private val _actualTotalItemsForResults = MutableStateFlow(0)
    val actualTotalItemsForResults: StateFlow<Int> = _actualTotalItemsForResults.asStateFlow()

    private val _answeredItemsList = mutableListOf<AnsweredFakeLoginItemDetails>()

    init {
        loadGameItems()


        viewModelScope.launch {
            gameItems.collectLatest { items ->
                updateCurrentItem(items, _currentItemIndex.value)
            }
        }
        viewModelScope.launch {
            _currentItemIndex.asStateFlow().collectLatest { index ->
                updateCurrentItem(gameItems.value, index)
            }
        }
    }

    private fun updateCurrentItem(items: List<FakeLoginGameItem>, index: Int) {
        if (items.isNotEmpty() && index >= 0 && index < items.size) {
            _currentItem.value = items[index]
        } else {
            _currentItem.value = null
        }
    }

    fun loadGameItems() {
        viewModelScope.launch {
            _isLoading.value = true
            _score.value = 0
            _currentItemIndex.value = 0
            _userAnswered.value = false
            _isCorrect.value = null
            _showResults.value = false
            _answeredItemsList.clear()
            dao.getAllItems().collectLatest { items ->
                _gameItems.value = items.shuffled()
                _actualTotalItemsForResults.value = _gameItems.value.size
                _isLoading.value = false
            }
        }
    }

    fun selectAnswer(userChoseFake: Boolean) {
        if (_userAnswered.value) return

        val current = _currentItem.value
        current?.let { item ->
            _userAnswered.value = true
            val correct = (item.isFake == userChoseFake)
            if (correct) {
                _score.value++
            }
            _isCorrect.value = correct

            val details = AnsweredFakeLoginItemDetails(
                fakeLoginItem = item,
                userAnswerWasFake = userChoseFake,
                wasCorrect = correct
            )
            _answeredItemsList.add(details)
        }
    }

    fun nextItem() {
        if (_currentItemIndex.value < _gameItems.value.size - 1) {
            _currentItemIndex.value++
            _userAnswered.value = false
            _isCorrect.value = null
        } else {
            ReviewDataHolder.answeredFakeLoginItems = ArrayList(_answeredItemsList)
            _currentItem.value = null
            _gameItems.value = emptyList()
            _currentItemIndex.value = 0
            _showResults.value = true
        }
    }

    fun onResultsNavigated() {
        _showResults.value = false
    }

    fun resetGame() {
        loadGameItems()
    }

    companion object {
        fun provideFactory(dao: FakeLoginGameItemDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(FakeLoginViewModel::class.java)) {
                        return FakeLoginViewModel(dao) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
