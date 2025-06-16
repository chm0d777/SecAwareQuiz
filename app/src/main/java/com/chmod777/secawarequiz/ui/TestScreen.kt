package com.chmod777.secawarequiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.Screen

@Composable
fun TestScreen(
    navController: NavHostController,
    testViewModel: TestViewModel,
    questionId: Int
) {
    val currentQuestion by testViewModel.currentQuestion.collectAsState()
    val answerGiven by testViewModel.answerGiven.collectAsState()
    val isCorrect by testViewModel.isCorrect.collectAsState()
    val explanationText by testViewModel.explanationText.collectAsState()
    val score by testViewModel.score.collectAsState()
    val totalQuestions = testViewModel.allQuestions.collectAsState().value.size
    val navigateToResults by testViewModel.navigateToResults.collectAsState()

    LaunchedEffect(questionId) {
        testViewModel.loadQuestionById(questionId)
    }

    LaunchedEffect(navigateToResults) {
        if (navigateToResults) {
            navController.navigate(Screen.QuizResults.createRoute(score, totalQuestions)) {
                popUpTo(Screen.Home.route)
            }
            testViewModel.onResultsNavigated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        currentQuestion?.let { question ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.test_screen_is_url_safe_question),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = question.url,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { testViewModel.onAnswerSelected(true) },
                    enabled = !answerGiven,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(stringResource(R.string.common_safe))
                }
                Button(
                    onClick = { testViewModel.onAnswerSelected(false) },
                    enabled = !answerGiven,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(stringResource(R.string.common_unsafe))
                }
            }

            if (answerGiven) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                                         else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.size(80.dp)) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                                contentDescription = if (isCorrect) stringResource(R.string.test_screen_correct_answer_desc) else stringResource(R.string.test_screen_incorrect_answer_desc),
                                modifier = Modifier.fillMaxSize(),
                                tint = if (isCorrect) MaterialTheme.colorScheme.onTertiaryContainer
                                       else MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isCorrect) stringResource(R.string.test_screen_correct_exclamation) else stringResource(R.string.test_screen_incorrect_exclamation),
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (isCorrect) MaterialTheme.colorScheme.onTertiaryContainer
                                    else MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = explanationText,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = if (isCorrect) MaterialTheme.colorScheme.onTertiaryContainer
                                    else MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { testViewModel.goToNextQuestion() },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(stringResource(R.string.quiz_screen_next_question_button))
                }
            }
        } ?: run {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
            Text(
                text = stringResource(R.string.common_loading),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
