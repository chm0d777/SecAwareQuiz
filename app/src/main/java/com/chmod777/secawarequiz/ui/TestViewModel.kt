package com.chmod777.secawarequiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.data.QuestionDao
import com.chmod777.secawarequiz.data.QuizQuestion
import com.chmod777.secawarequiz.data.ReviewDataHolder /
import com.chmod777.secawarequiz.data.model.AnsweredQuestionDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TestViewModel(private val questionDao: QuestionDao) : ViewModel() {

    private var currentQuizLoadingJob: Job? = null

    private val _currentQuestion = MutableStateFlow<QuizQuestion?>(null)
    val currentQuestion: StateFlow<QuizQuestion?> = _currentQuestion.asStateFlow()

    private val _answerGiven = MutableStateFlow(false)
    val answerGiven: StateFlow<Boolean> = _answerGiven.asStateFlow()

    private val _isCorrect = MutableStateFlow(false)
    val isCorrect: StateFlow<Boolean> = _isCorrect.asStateFlow()

    private val _explanationText = MutableStateFlow("")
    val explanationText: StateFlow<String> = _explanationText.asStateFlow()

    private val _allQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val allQuestions: StateFlow<List<QuizQuestion>> = _allQuestions.asStateFlow()


    private var currentQuestionIndex: Int = 0

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()


    private val _currentQuestionUiIndex = MutableStateFlow(0)
    val currentQuestionUiIndex: StateFlow<Int> = _currentQuestionUiIndex.asStateFlow()


    private val _actualTotalQuestionsForResults = MutableStateFlow(0)
    val actualTotalQuestionsForResults: StateFlow<Int> = _actualTotalQuestionsForResults.asStateFlow()

    private val _navigateToResults = MutableStateFlow(false)
    val navigateToResults: StateFlow<Boolean> = _navigateToResults.asStateFlow()

    private val _answeredQuestionDetailsList = mutableListOf<AnsweredQuestionDetails>()


    fun loadQuizByInternalId(internalQuizId: Int) {
        currentQuizLoadingJob?.cancel()

        val quizGroupId = when (internalQuizId) {
            1 -> "URL_SAFETY"
            2 -> "PASSWORD_STRENGTH"
            3 -> "SOCIAL_ENGINEERING"
            else -> "UNKNOWN_QUIZ"
        }


        _navigateToResults.value = false
        _score.value = 0
        _answeredQuestionDetailsList.clear()
        resetAnswerState()


        _allQuestions.value = emptyList()
        _currentQuestion.value = null
        _currentQuestionUiIndex.value = 0


        currentQuizLoadingJob = viewModelScope.launch {
            questionDao.getQuestionsByGroupId(quizGroupId).collect { questionsFromDb ->

                if (!isActive) return@collect

                _allQuestions.value = questionsFromDb


                val currentQuestions = _allQuestions.value
                if (currentQuestions.isNotEmpty()) {
                    _actualTotalQuestionsForResults.value = currentQuestions.size
                    currentQuestionIndex = 0
                    _currentQuestionUiIndex.value = currentQuestionIndex
                    _currentQuestion.value = currentQuestions[currentQuestionIndex]
                    _navigateToResults.value = false
                } else {
                    _actualTotalQuestionsForResults.value = 0
                    _currentQuestion.value = null
                    _currentQuestionUiIndex.value = 0
                    _navigateToResults.value = true
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
            _navigateToResults.value = true
            return
        }
        val nextIndex = currentQuestionIndex + 1
        if (nextIndex < questions.size) {
            currentQuestionIndex = nextIndex
            _currentQuestionUiIndex.value = currentQuestionIndex
            _currentQuestion.value = questions[currentQuestionIndex]
            resetAnswerState()
        } else {
            ReviewDataHolder.answeredQuestions = ArrayList(_answeredQuestionDetailsList)
            _currentQuestion.value = null
            _allQuestions.value = emptyList()
            _currentQuestionUiIndex.value = 0
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
