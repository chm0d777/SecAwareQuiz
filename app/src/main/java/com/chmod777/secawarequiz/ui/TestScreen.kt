package com.chmod777.secawarequiz.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color // Keep for figmaColor definitions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.data.QuestionDao
import com.chmod777.secawarequiz.data.QuizQuestion
import com.chmod777.secawarequiz.data.UrlCheckQuestion
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
// Removed figma color definitions as they are not used or replaced by MaterialTheme

@Composable
fun TestScreen(
    navController: NavHostController,
    testViewModel: TestViewModel,
    internalQuizId: Int
) {
    val currentQuestion by testViewModel.currentQuestion.collectAsState()
    // IMPORTANT: Use a distinct state variable for the LaunchedEffect condition
    val navigateToResultsState by testViewModel.navigateToResults.collectAsState()
    val explanationText by testViewModel.explanationText.collectAsState()
    val answerGiven by testViewModel.answerGiven.collectAsState()
    val isCorrect by testViewModel.isCorrect.collectAsState()
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }

    val currentQuestionUiIndex by testViewModel.currentQuestionUiIndex.collectAsState()
    val allQuestionsForQuiz by testViewModel.allQuestions.collectAsState()
    // val totalQuestionsInQuiz = allQuestionsForQuiz.size // This will become 0 when navigating
    // Replaced by actualTotalForNavigation for the navigation call.
    // totalQuestionsInQuiz is still used by QuestionContentDisplay and will be 0 when navigating.

    val scoreForResults by testViewModel.score.collectAsState()
    val actualTotalForNavigation by testViewModel.actualTotalQuestionsForResults.collectAsState()

    LaunchedEffect(internalQuizId) {
        testViewModel.loadQuizByInternalId(internalQuizId)
    }

    // Observe the distinct state for navigation
    LaunchedEffect(navigateToResultsState) {
        if (navigateToResultsState) {
            // Optional: kotlinx.coroutines.delay(16) // Delay for one frame
            navController.navigate(Screen.QuizResults.createRoute(scoreForResults, actualTotalForNavigation)) {
                popUpTo(Screen.Home.route)
            }
            testViewModel.onResultsNavigated() // Reset the flag in ViewModel
        }
    }

    // Conditional rendering for the entire screen content
    if (navigateToResultsState) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Use theme color
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Переход к результатам...", // Assume this string resource exists or add it
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )
            }
        }
    } else {
        // Original Column content
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Text(
                text = stringResource(R.string.test_screen_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp)
            )

            // No early return here anymore based on navigateToResults
            // The null check for questionData will handle the loading state naturally

            val questionData = currentQuestion

            if (questionData == null) { // This handles initial loading AND the state where quiz is finished
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Use theme color
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.test_screen_loading_question),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                QuestionContentDisplay(
                    question = questionData,
                    currentQuestionDisplayIndex = currentQuestionUiIndex,
                    totalQuestionsInDisplayQuiz = allQuestionsForQuiz.size, // Use current size for display
                    selectedOptionIndex = selectedOptionIndex,
                    answerGiven = answerGiven,
                    isCorrect = isCorrect,
                    explanationText = explanationText,
                    onOptionClick = { index ->
                        selectedOptionIndex = index
                        testViewModel.onAnswerSelected(index)
                    },
                    onNextClick = {
                        testViewModel.goToNextQuestion()
                        selectedOptionIndex = null
                    }
                )
            }
        }
    }
}

@Composable
private fun QuestionContentDisplay(
    question: QuizQuestion,
    currentQuestionDisplayIndex: Int,
    totalQuestionsInDisplayQuiz: Int,
    selectedOptionIndex: Int?,
    answerGiven: Boolean,
    isCorrect: Boolean?,
    explanationText: String,
    onOptionClick: (Int) -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        if (totalQuestionsInDisplayQuiz > 0) {
            Text(
                text = stringResource(R.string.test_screen_question_progress, currentQuestionDisplayIndex + 1, totalQuestionsInDisplayQuiz),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)) // Use theme color
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(
                            if (totalQuestionsInDisplayQuiz > 0) (currentQuestionDisplayIndex + 1).toFloat() / totalQuestionsInDisplayQuiz else 0f
                        )
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp)) // Use theme color
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = question.text,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            question.options.forEachIndexed { index, optionText ->
                val isCurrentlySelected = selectedOptionIndex == index
                val isCorrectOption = index == question.correctAnswerIndex

                val optionCardBackgroundColor = when {
                    answerGiven && isCorrectOption -> Color(0xFF4CAF50) // figmaOptionCorrectBg
                    answerGiven && isCurrentlySelected && isCorrect == false -> Color(0xFFF44336) // figmaOptionIncorrectBg
                    !answerGiven && isCurrentlySelected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                val optionTextColor = when {
                    answerGiven && (isCorrectOption || (isCurrentlySelected && isCorrect == false)) -> Color.White
                    !answerGiven && isCurrentlySelected -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                val optionBorderColor = if (!answerGiven && isCurrentlySelected) MaterialTheme.colorScheme.primary else Color.Transparent

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable(enabled = !answerGiven) { onOptionClick(index) },
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = optionCardBackgroundColor),
                    border = BorderStroke(if (optionBorderColor != Color.Transparent) 2.dp else 0.dp, optionBorderColor)
                ) {
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!answerGiven && isCurrentlySelected) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary, // figmaOptionSelectedIconColor
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                        } else if (answerGiven && isCurrentlySelected) {
                            Icon(
                                imageVector = if (isCorrect == true) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                                contentDescription = if (isCorrect == true) "Correct" else "Incorrect",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                        } else if (answerGiven && isCorrectOption) {
                             Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Correct Answer Icon",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                        } else {
                            Spacer(Modifier.width(32.dp))
                        }
                        Text(text = optionText, color = optionTextColor, fontSize = 16.sp, modifier = Modifier.weight(1f))
                    }
                }
            }
            if (answerGiven) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = explanationText,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNextClick,
            enabled = answerGiven,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.test_screen_next_question),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestScreenPreview() {
    val previewDao = QuestionDaoPreview()
    val testViewModel = TestViewModel(previewDao)

    LaunchedEffect(Unit) {
        testViewModel.loadQuizByInternalId(2)
    }

    SecAwareQuizTheme {
        TestScreen(
            navController = rememberNavController(),
            testViewModel = testViewModel,
            internalQuizId = 2
        )
    }
}

private class QuestionDaoPreview : QuestionDao {
    private val sampleQuizQuestions = listOf(
        QuizQuestion(id = 1, quizGroupId = "URL_SAFETY", text = "Preview: What is phishing?", options = listOf("A fish", "A scam", "A sport"), correctAnswerIndex = 1, explanation = "It's a scam to steal info."),
        QuizQuestion(id = 2, quizGroupId = "PASSWORD_STRENGTH", text = "Preview: Secure password?", options = listOf("12345", "P@\$\$wOrd!", "qwerty"), correctAnswerIndex = 1, explanation = "Complex is better."),
        QuizQuestion(id = 4, quizGroupId = "PASSWORD_STRENGTH", text = "Preview: What NOT to use in password?", options = listOf("Symbols", "Birth date"), correctAnswerIndex = 1, explanation = "Personal info is bad."),
        QuizQuestion(id = 3, quizGroupId = "SOCIAL_ENGINEERING", text = "Preview: What is 2FA?", options = listOf("Two factors", "Two friends"), correctAnswerIndex = 0, explanation = "Two-factor authentication.")
    )

    override fun getAllQuizQuestions(): Flow<List<QuizQuestion>> = flowOf(sampleQuizQuestions)

    override fun getQuizQuestionById(id: Int): Flow<QuizQuestion?> = flowOf(
        sampleQuizQuestions.find { it.id == id }
    )

    override fun getQuestionsByGroupId(groupId: String): Flow<List<QuizQuestion>> {
        return flowOf(sampleQuizQuestions.filter { it.quizGroupId == groupId }.sortedBy { it.id })
    }

    override suspend fun insertQuizQuestion(question: QuizQuestion) {}
    override suspend fun insertAllQuizQuestions(questions: List<QuizQuestion>) {}
    override suspend fun insertQuestion(question: UrlCheckQuestion) {}
    override fun getAllQuestions(): Flow<List<UrlCheckQuestion>> = emptyFlow()
    override suspend fun getQuestionById(id: Int): UrlCheckQuestion? = null
    override suspend fun deleteAllQuestions() {}
}
