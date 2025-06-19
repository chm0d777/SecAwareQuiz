package com.chmod777.secawarequiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.data.QuestionDao
import com.chmod777.secawarequiz.data.QuizQuestion
import com.chmod777.secawarequiz.data.ReviewDataHolder // Added import
import com.chmod777.secawarequiz.data.model.AnsweredQuestionDetails // Added import
import kotlinx.coroutines.Job // Added import
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive // Added import
import kotlinx.coroutines.launch

class TestViewModel(private val questionDao: QuestionDao) : ViewModel() {

    private var currentQuizLoadingJob: Job? = null // Added job member

    private val _currentQuestion = MutableStateFlow<QuizQuestion?>(null)
    val currentQuestion: StateFlow<QuizQuestion?> = _currentQuestion.asStateFlow()

    private val _answerGiven = MutableStateFlow(false)
    val answerGiven: StateFlow<Boolean> = _answerGiven.asStateFlow()

    private val _isCorrect = MutableStateFlow(false)
    val isCorrect: StateFlow<Boolean> = _isCorrect.asStateFlow()

    private val _explanationText = MutableStateFlow("")
    val explanationText: StateFlow<String> = _explanationText.asStateFlow()

    private val _allQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val allQuestions: StateFlow<List<QuizQuestion>> = _allQuestions.asStateFlow() // Will hold questions for the current group

    // private var currentQuestionOriginalId: Int = -1 // No longer needed with groupId approach
    private var currentQuestionIndex: Int = 0 // Index within the current group

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    // New StateFlow for UI display of current question number (1-based)
    private val _currentQuestionUiIndex = MutableStateFlow(0)
    val currentQuestionUiIndex: StateFlow<Int> = _currentQuestionUiIndex.asStateFlow()
    // _totalQuestionsInDisplayQuiz can be derived from _allQuestions.size() in the UI directly

    private val _navigateToResults = MutableStateFlow(false)
    val navigateToResults: StateFlow<Boolean> = _navigateToResults.asStateFlow()

    private val _answeredQuestionDetailsList = mutableListOf<AnsweredQuestionDetails>() // Added list

    init {
        // Initial loading logic can be simplified or removed if loadQuizByInternalId is always called first.
        // For now, keeping it empty as loadQuizByInternalId will be the entry point.
    }

    fun loadQuizByInternalId(internalQuizId: Int) {
        currentQuizLoadingJob?.cancel() // Cancel any ongoing collection

        val quizGroupId = when (internalQuizId) {
            1 -> "URL_SAFETY"
            2 -> "PASSWORD_STRENGTH"
            3 -> "SOCIAL_ENGINEERING"
            else -> "UNKNOWN_QUIZ" // Or handle error appropriately
        }

        // Reset states for the new quiz loading sequence
        _navigateToResults.value = false
        _score.value = 0
        _answeredQuestionDetailsList.clear()
        resetAnswerState() // Resets _answerGiven, _isCorrect, _explanationText

        // Indicate loading state by clearing previous questions
        _allQuestions.value = emptyList()
        _currentQuestion.value = null
        _currentQuestionUiIndex.value = 0


        currentQuizLoadingJob = viewModelScope.launch {
            questionDao.getQuestionsByGroupId(quizGroupId).collect { questionsFromDb ->
                // Check if the coroutine is still active before processing
                // This is important if the job was cancelled by a new call to loadQuizByInternalId
                if (!isActive) return@collect

                _allQuestions.value = questionsFromDb

                // Use the latest value from _allQuestions state flow
                val currentQuestions = _allQuestions.value
                if (currentQuestions.isNotEmpty()) {
                    currentQuestionIndex = 0
                    _currentQuestionUiIndex.value = currentQuestionIndex // UI index is 0-based here
                    _currentQuestion.value = currentQuestions[currentQuestionIndex]
                    _navigateToResults.value = false // Questions loaded, so not navigating to results
                } else {
                    _currentQuestion.value = null
                    _currentQuestionUiIndex.value = 0 // Or -1 if appropriate for UI
                    _navigateToResults.value = true // No questions found, navigate to results
                }
            }
        }
    }

    private fun resetAnswerState() {
        _answerGiven.value = false
        _isCorrect.value = false
        _explanationText.value = ""
    }

    fun onAnswerSelected(selectedIndex: Int) {
        val question = _currentQuestion.value ?: return
        if (_answerGiven.value) return

        _answerGiven.value = true
        val correctAnswerResult = selectedIndex == question.correctAnswerIndex
        _isCorrect.value = correctAnswerResult
        if (correctAnswerResult) {
            _score.value++
        }
        _explanationText.value = question.explanation

        // Store answered question details
        val details = AnsweredQuestionDetails(
            question = question,
            userAnswerIndex = selectedIndex,
            wasCorrect = correctAnswerResult
        )
        _answeredQuestionDetailsList.add(details)
    }

    fun goToNextQuestion() {
        val questions = _allQuestions.value
        if (questions.isEmpty()) {
            _navigateToResults.value = true // Should be handled by loadQuizByInternalId if group is empty
            return
        }
        val nextIndex = currentQuestionIndex + 1
        if (nextIndex < questions.size) {
            currentQuestionIndex = nextIndex
            _currentQuestionUiIndex.value = currentQuestionIndex // Update UI index
            _currentQuestion.value = questions[currentQuestionIndex]
            resetAnswerState()
        } else {
            ReviewDataHolder.answeredQuestions = ArrayList(_answeredQuestionDetailsList)
            _currentQuestion.value = null
            _allQuestions.value = emptyList() // Also clear all questions list
            _currentQuestionUiIndex.value = 0 // Reset UI index
            _navigateToResults.value = true
        }
    }

    fun onResultsNavigated() {
        _navigateToResults.value = false
    }

    companion object {
        fun provideFactory(questionDao: QuestionDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TestViewModel(questionDao) as T
                }
            }
    }
}
