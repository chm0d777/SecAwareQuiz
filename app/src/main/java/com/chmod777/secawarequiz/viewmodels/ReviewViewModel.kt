package com.chmod777.secawarequiz.viewmodels

import androidx.lifecycle.ViewModel
import com.chmod777.secawarequiz.data.ReviewDataHolder
import com.chmod777.secawarequiz.data.model.AnsweredQuestionDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReviewViewModel : ViewModel() {
    private val _answeredQuestions = MutableStateFlow<List<AnsweredQuestionDetails>>(emptyList())
    val answeredQuestions: StateFlow<List<AnsweredQuestionDetails>> = _answeredQuestions.asStateFlow()

    init {
        loadAnsweredQuestions()
    }

    private fun loadAnsweredQuestions() {
        _answeredQuestions.value = ReviewDataHolder.answeredQuestions ?: emptyList()

    }

    fun clearReviewData() {

        ReviewDataHolder.answeredQuestions = null
        _answeredQuestions.value = emptyList()
    }
}
