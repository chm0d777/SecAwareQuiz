package com.chmod777.secawarequiz.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme

data class QuizQuestion(
    val id: String,
    val text: String,
    val options: List<QuizOption>,
    val correctAnswerId: String,
    val explanation: String? = null
)

data class QuizOption(
    val id: String,
    val text: String
)

data class QuizScreenState(
    val currentQuestion: QuizQuestion?,
    val selectedOptionId: String?,
    val isAnswerSubmitted: Boolean,
    val isCorrect: Boolean?,
    val score: Int,
    val currentQuestionIndex: Int,
    val totalQuestions: Int,
    val isQuizOver: Boolean,
    val isLoading: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
) {
    val dummyQuestion = QuizQuestion(
        id = "q1",
        text = "Какова основная цель двухфакторной аутентификации (2FA)?",
        options = listOf(
            QuizOption("opt1", "Сделать пароли длиннее и сложнее."),
            QuizOption("opt2", "Добавить дополнительный уровень безопасности помимо пароля."),
            QuizOption("opt3", "Зашифровать ваше интернет-соединение."),
            QuizOption("opt4", "Ускорить процесс входа.")
        ),
        correctAnswerId = "opt2",
        explanation = "2FA добавляет второй этап проверки, например код с вашего телефона, что затрудняет доступ злоумышленникам, даже если у них есть ваш пароль."
    )

    val state = QuizScreenState(
        currentQuestion = dummyQuestion,
        selectedOptionId = null,
        isAnswerSubmitted = false,
        isCorrect = null,
        score = 0,
        currentQuestionIndex = 0,
        totalQuestions = 5,
        isQuizOver = false,
        isLoading = false
    )
    var selectedOptionIdInternal by remember { mutableStateOf(state.selectedOptionId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (state.isQuizOver) stringResource(R.string.quiz_screen_results_title)
                        else stringResource(R.string.quiz_screen_question_progress_format, state.currentQuestionIndex + 1, state.totalQuestions)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.isQuizOver) {
            QuizResultsContent(
                score = state.score,
                totalQuestions = state.totalQuestions,
                onPlayAgain = { },
                onBackToHome = { },
                modifier = Modifier.padding(paddingValues)
            )
        } else if (state.currentQuestion != null) {
            QuizQuestionContent(
                question = state.currentQuestion,
                selectedOptionId = selectedOptionIdInternal,
                onOptionSelected = { optionId ->
                    if (!state.isAnswerSubmitted) {
                        selectedOptionIdInternal = optionId
                    }
                },
                isAnswerSubmitted = state.isAnswerSubmitted,
                isCorrect = state.isCorrect,
                onSubmitAnswer = { },
                onNextQuestion = {
                    selectedOptionIdInternal = null
                },
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.quiz_screen_no_question_or_over), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun QuizQuestionContent(
    question: QuizQuestion,
    selectedOptionId: String?,
    onOptionSelected: (String) -> Unit,
    isAnswerSubmitted: Boolean,
    isCorrect: Boolean?,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = question.text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )
        }

        items(question.options.size, key = { question.options[it].id }) { index ->
            val option = question.options[index]
            val isSelected = option.id == selectedOptionId
            val baseBorderColor = MaterialTheme.colorScheme.outline
            val interactionBorderColor = when {
                !isAnswerSubmitted -> MaterialTheme.colorScheme.primary
                isSelected && isCorrect == true -> MaterialTheme.colorScheme.primary
                isSelected && isCorrect == false -> MaterialTheme.colorScheme.error
                else -> baseBorderColor
            }
            val backgroundColor = when {
                !isAnswerSubmitted && isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                isAnswerSubmitted && isSelected && isCorrect == true -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                isAnswerSubmitted && isSelected && isCorrect == false -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = { onOptionSelected(option.id) },
                        enabled = !isAnswerSubmitted
                    ),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, if (isSelected || isAnswerSubmitted) interactionBorderColor else baseBorderColor),
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onOptionSelected(option.id) },
                        enabled = !isAnswerSubmitted,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = if (isAnswerSubmitted && isCorrect == false && isSelected) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(text = option.text, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            if (isAnswerSubmitted) {
                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.quiz_screen_next_question_button))
                }
                if (question.explanation != null && isCorrect != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(if (isCorrect) R.string.quiz_screen_correct_prefix else R.string.quiz_screen_incorrect_prefix) + question.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                Button(
                    onClick = onSubmitAnswer,
                    enabled = selectedOptionId != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.quiz_screen_submit_answer_button))
                }
            }
        }
    }
}

@Composable
fun QuizResultsContent(
    score: Int,
    totalQuestions: Int,
    onPlayAgain: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.quiz_screen_test_completed), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("${stringResource(R.string.quiz_screen_your_score_label)} $score / $totalQuestions", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onPlayAgain, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.common_play_again))
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onBackToHome, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.quiz_screen_back_to_main_menu_button))
        }
    }
}

@Preview(showBackground = true, name = "Показать")
@Composable
fun QuizScreenQuestionPreview() {
    SecAwareQuizTheme {
        QuizScreen()
    }
}

@Preview(showBackground = true, name = "Правильный ответ")
@Composable
fun QuizScreenAnswerCorrectPreview() {
    SecAwareQuizTheme {
        val question = QuizQuestion(
            id = "q1", text = "Что такое фишинг?",
            options = listOf(QuizOption("opt1", "Option 1"), QuizOption("opt2", "Пральна")),
            correctAnswerId = "opt2",
            explanation = "объясняю."
        )
        QuizScreen()
    }
}

@Preview(showBackground = true, name = "Результаты")
@Composable
fun QuizScreenResultsPreview() {
    SecAwareQuizTheme {
        QuizResultsContent(score = 3, totalQuestions = 5, onPlayAgain={}, onBackToHome={})
    }
}
