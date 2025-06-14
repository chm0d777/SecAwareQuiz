package com.chmod777.secawarequiz.viewmodels.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.ui.screens.quiz.QuizOption
import com.chmod777.secawarequiz.ui.screens.quiz.QuizQuestion
import com.chmod777.secawarequiz.ui.screens.quiz.QuizScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class QuizViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        QuizScreenState(
            currentQuestion = null, selectedOptionId = null, isAnswerSubmitted = false,
            isCorrect = null, score = 0, currentQuestionIndex = 0, totalQuestions = 0,
            isQuizOver = false, isLoading = true
        )
    )
    val uiState: StateFlow<QuizScreenState> = _uiState.asStateFlow()

    private var allQuestions: List<QuizQuestion> = emptyList()
    private var currentQuizId: String? = null

    init {
        loadDummyQuiz()
    }

    private fun loadDummyQuiz() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(500)
            allQuestions = listOf(
                QuizQuestion("q1", "What is malware?", listOf(QuizOption("o1_1", "Friendly software"), QuizOption("o1_2", "Malicious software")), "o1_2", "Malware is software designed to harm your computer."),
                QuizQuestion("q2", "What does HTTPS stand for?", listOf(QuizOption("o2_1", "HyperText Transfer Protocol Secure"), QuizOption("o2_2", "HyperText Transfer Protocol Standard")), "o2_1", "HTTPS encrypts data between your browser and the website.")
            )
            if (allQuestions.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentQuestion = allQuestions[0],
                    currentQuestionIndex = 0,
                    totalQuestions = allQuestions.size,
                    score = 0,
                    isQuizOver = false,
                    selectedOptionId = null,
                    isAnswerSubmitted = false,
                    isCorrect = null
                )
            } else {
                 _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onOptionSelected(optionId: String) {
        if (!_uiState.value.isAnswerSubmitted) {
            _uiState.value = _uiState.value.copy(selectedOptionId = optionId)
        }
    }

    fun onSubmitAnswer() {
        val currentState = _uiState.value
        if (currentState.currentQuestion == null || currentState.selectedOptionId == null || currentState.isAnswerSubmitted) {
            return
        }

        val isCorrect = currentState.currentQuestion.correctAnswerId == currentState.selectedOptionId
        val newScore = if (isCorrect) currentState.score + 1 else currentState.score

        _uiState.value = currentState.copy(
            isAnswerSubmitted = true,
            isCorrect = isCorrect,
            score = newScore
        )
    }

    fun onNextQuestion() {
        val currentState = _uiState.value
        val nextIndex = currentState.currentQuestionIndex + 1

        if (nextIndex < currentState.totalQuestions) {
            _uiState.value = currentState.copy(
                currentQuestionIndex = nextIndex,
                currentQuestion = allQuestions[nextIndex],
                selectedOptionId = null,
                isAnswerSubmitted = false,
                isCorrect = null
            )
        } else {
            _uiState.value = currentState.copy(
                isQuizOver = true,
                currentQuestion = null
            )
        }
    }

    fun restartQuiz() {
        loadDummyQuiz()
    }
}
