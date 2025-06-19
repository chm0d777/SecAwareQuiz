package com.chmod777.secawarequiz.viewmodels

import androidx.lifecycle.ViewModel
import com.chmod777.secawarequiz.data.ReviewDataHolder
import com.chmod777.secawarequiz.data.model.AnsweredGameItemDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameReviewViewModel : ViewModel() {
    private val _answeredGameItems = MutableStateFlow<List<AnsweredGameItemDetails>>(emptyList())
    val answeredGameItems: StateFlow<List<AnsweredGameItemDetails>> = _answeredGameItems.asStateFlow()

    init {
        _answeredGameItems.value = ReviewDataHolder.answeredGameItems ?: emptyList()
    }

    fun clearReviewData() {
        // Using the specific clear method from ReviewDataHolder
        ReviewDataHolder.clearGameReviewData()
        _answeredGameItems.value = emptyList()
    }
}
