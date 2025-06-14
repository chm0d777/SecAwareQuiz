package com.chmod777.secawarequiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.ui.TestViewModel

@Composable
fun TestScreen(
    navController: NavHostController,
    testViewModel: TestViewModel = viewModel(),
    questionId: Int
) {
    val currentQuestion by testViewModel.currentQuestion.collectAsState()
    val answerGiven by testViewModel.answerGiven.collectAsState()
    val isCorrect by testViewModel.isCorrect.collectAsState()
    val explanationText by testViewModel.explanationText.collectAsState()

    LaunchedEffect(questionId) {
        testViewModel.loadQuestionById(questionId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentQuestion != null) {
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
                        text = "Этот URL безопасен?",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = currentQuestion!!.url,
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
                    Text("Безопасно")
                }
                Button(
                    onClick = { testViewModel.onAnswerSelected(false) },
                    enabled = !answerGiven,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text("Небезопасно")
                }
            }

            if (answerGiven) {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE))
                        .padding(16.dp)
                        .background(
                            color = if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            shape = MaterialTheme.shapes.medium
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isCorrect) "Правильно!" else "Неправильно!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = explanationText,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { testViewModel.goToNextQuestion() },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Следующий вопрос")
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Тест завершен! норм!",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(onClick = { navController.popBackStack() }) {
                    Text("Вернуться в главное меню")
                }
            }
        }
    }
}
