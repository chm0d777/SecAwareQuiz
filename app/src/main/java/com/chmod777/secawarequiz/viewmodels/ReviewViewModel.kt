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
        // Optional: Clear ReviewDataHolder.answeredQuestions here if it should only be consumed once.
        // For this implementation, let's clear it when explicitly leaving the screen or done.
        // ReviewDataHolder.answeredQuestions = null
    }

    fun clearReviewData() {
        // Call this if the data should be cleared when done reviewing,
        // e.g. when explicitly navigating away from the review screen.
        ReviewDataHolder.answeredQuestions = null
        _answeredQuestions.value = emptyList()
    }
}
