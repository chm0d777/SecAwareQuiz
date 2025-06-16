package com.chmod777.secawarequiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.data.UrlCheckQuestion
import com.chmod777.secawarequiz.data.QuestionDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TestViewModel(private val questionDao: QuestionDao) : ViewModel() {

    private val _currentQuestion = MutableStateFlow<UrlCheckQuestion?>(null)
    val currentQuestion: StateFlow<UrlCheckQuestion?> = _currentQuestion.asStateFlow()

    private val _answerGiven = MutableStateFlow(false)
    val answerGiven: StateFlow<Boolean> = _answerGiven.asStateFlow()

    private val _isCorrect = MutableStateFlow(false)
    val isCorrect: StateFlow<Boolean> = _isCorrect.asStateFlow()

    private val _explanationText = MutableStateFlow("")
    val explanationText: StateFlow<String> = _explanationText.asStateFlow()

    private val _allQuestions = MutableStateFlow<List<UrlCheckQuestion>>(emptyList())
    val allQuestions: StateFlow<List<UrlCheckQuestion>> = _allQuestions.asStateFlow()

    private var currentQuestionOriginalId: Int = -1
    private var questionIndex: Int = 0

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _navigateToResults = MutableStateFlow(false)
    val navigateToResults: StateFlow<Boolean> = _navigateToResults.asStateFlow()

    init {
        viewModelScope.launch {
            questionDao.getAllQuestions().collect { questions ->
                _allQuestions.value = questions
                if (currentQuestionOriginalId != -1 && _currentQuestion.value == null && questions.isNotEmpty()) {
                    val questionById = questions.find { it.id == currentQuestionOriginalId }
                    if (questionById != null) {
                        _currentQuestion.value = questionById
                        questionIndex = questions.indexOf(questionById)
                    } else {
                        _currentQuestion.value = questions.firstOrNull()
                        questionIndex = 0
                    }
                } else if (_currentQuestion.value == null && questions.isNotEmpty()){
                     _currentQuestion.value = questions.firstOrNull()
                     questionIndex = 0
                }
                _score.value = 0
                _navigateToResults.value = false
            }
        }
    }

    fun loadQuestionById(id: Int) {
        currentQuestionOriginalId = id
        _navigateToResults.value = false
        _score.value = 0

        viewModelScope.launch {
            if (_allQuestions.value.isNotEmpty()) {
                val question = _allQuestions.value.find { it.id == id }
                if (question != null) {
                    _currentQuestion.value = question
                    questionIndex = _allQuestions.value.indexOf(question)
                } else {
                    _currentQuestion.value = _allQuestions.value.firstOrNull()
                    questionIndex = 0
                }
            } else {
                 _currentQuestion.value = null
                 val questionFromDb = questionDao.getQuestionById(id)
                 if (_allQuestions.value.isEmpty()) {
                    _currentQuestion.value = questionFromDb
                 }
            }
            resetAnswerState()
        }
    }

    private fun resetAnswerState() {
        _answerGiven.value = false
        _isCorrect.value = false
        _explanationText.value = ""
    }

    fun onAnswerSelected(userAnswer: Boolean) {
        val question = _currentQuestion.value ?: return
        if (_answerGiven.value) return

        _answerGiven.value = true
        val correctAnswer = userAnswer == question.isSecure
        _isCorrect.value = correctAnswer
        if (correctAnswer) {
            _score.value++
        }
        _explanationText.value = question.explanation
    }

    fun goToNextQuestion() {
        val questions = _allQuestions.value
        if (questions.isEmpty()) {
            _navigateToResults.value = true
            return
        }

        val nextIndex = questionIndex + 1
        if (nextIndex < questions.size) {
            questionIndex = nextIndex
            _currentQuestion.value = questions[questionIndex]
            resetAnswerState()
        } else {
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
