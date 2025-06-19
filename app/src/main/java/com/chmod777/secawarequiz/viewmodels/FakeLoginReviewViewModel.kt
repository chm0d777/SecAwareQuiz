package com.chmod777.secawarequiz.viewmodels

import androidx.lifecycle.ViewModel
import com.chmod777.secawarequiz.data.ReviewDataHolder
import com.chmod777.secawarequiz.data.model.AnsweredFakeLoginItemDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeLoginReviewViewModel : ViewModel() {
    private val _answeredItems = MutableStateFlow<List<AnsweredFakeLoginItemDetails>>(emptyList())
    val answeredItems: StateFlow<List<AnsweredFakeLoginItemDetails>> = _answeredItems.asStateFlow()

    init {
        _answeredItems.value = ReviewDataHolder.answeredFakeLoginItems ?: emptyList()
    }

    fun clearReviewData() {
        ReviewDataHolder.clearFakeLoginReviewData()
        _answeredItems.value = emptyList()
    }
}
