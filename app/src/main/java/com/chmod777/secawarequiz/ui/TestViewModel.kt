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
    private val allQuestions: StateFlow<List<UrlCheckQuestion>> = _allQuestions.asStateFlow()

    private var questionIndex: Int = 0

    init {
        viewModelScope.launch {
            questionDao.getAllQuestions().collect { questions ->
                _allQuestions.value = questions
                if (questions.isNotEmpty()) {
                    _currentQuestion.value = questions[questionIndex]
                }
            }
        }
    }

    fun loadQuestionById(id: Int) {
        viewModelScope.launch {
            val question = questionDao.getQuestionById(id)
            if (question != null) {
                _currentQuestion.value = question
                _answerGiven.value = false
                _isCorrect.value = false
                _explanationText.value = ""
                questionIndex = _allQuestions.value.indexOfFirst { it.id == id }
            }
        }
    }

    fun onAnswerSelected(userAnswer: Boolean) {
        val question = _currentQuestion.value ?: return

        _answerGiven.value = true
        _isCorrect.value = (userAnswer == question.isSecure)
        _explanationText.value = question.explanation
    }

    fun goToNextQuestion() {
        val questions = _allQuestions.value
        if (questions.isNotEmpty() && questionIndex < questions.size - 1) {
            questionIndex++
            _currentQuestion.value = questions[questionIndex]
            _answerGiven.value = false
            _isCorrect.value = false
            _explanationText.value = ""
        } else {
            _currentQuestion.value = null
        }
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
