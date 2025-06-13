package com.chmod777.secawarequiz

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.data.Question

val sampleQuestions = listOf(
    Question(
        id = 1,
        text = "What is a common sign of a phishing email?",
        options = listOf(
            "An urgent request for personal information.",
            "A generic greeting like 'Dear Customer'.",
            "Spelling and grammar mistakes.",
            "All of the above."
        ),
        correctAnswerIndex = 3,
        explanation = "Phishing emails often try to create a sense of urgency, use generic greetings, and contain errors."
    ),
    Question(
        id = 2,
        text = "Which of the following is the strongest password?",
        options = listOf(
            "password123",
            "MyBirthDay1990!",
            "Tr0ub4dor&3",
            "johnsmith"
        ),
        correctAnswerIndex = 2,
        explanation = "'Tr0ub4dor&3' is the strongest as it includes a mix of uppercase, lowercase, numbers, and symbols, and is not easily guessable."
    ),
    Question(
        id = 3,
        text = "What does HTTPS in a website URL indicate?",
        options = listOf(
            "The website is 100% secure from all threats.",
            "The website has a fast connection.",
            "The connection between your browser and the website is encrypted.",
            "The website is hosted in the United States."
        ),
        correctAnswerIndex = 2,
        explanation = "HTTPS indicates that the data transferred between your browser and the website is encrypted, making it more secure than HTTP."
    ),
    Question(
        id = 4,
        text = "What should you do if you receive a suspicious email asking for your bank details?",
        options = listOf(
            "Reply with your details immediately.",
            "Click on any links to see where they go.",
            "Delete the email and do not respond or click any links.",
            "Call the phone number provided in the email to verify."
        ),
        correctAnswerIndex = 2,
        explanation = "Never provide personal details via email or click suspicious links. Contact your bank directly through their official website or phone number if you are concerned."
    )
)

private fun handleSubmitOrNext(
    context: Context,
    selectedOptionIndex: Int,
    answerSubmitted: Boolean,
    currentQuestion: Question,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    onAnswerSubmittedUpdate: (Boolean) -> Unit,
    onNextQuestion: () -> Unit,
    onShowResult: () -> Unit
) {
    if (selectedOptionIndex == -1 && !answerSubmitted) {
        Toast.makeText(context, "Please select an option.", Toast.LENGTH_SHORT).show()
        return
    }

    if (!answerSubmitted) {
        if (selectedOptionIndex == currentQuestion.correctAnswerIndex) {
            onScoreUpdate(score + 1)
            Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Incorrect. The correct answer was: ${currentQuestion.options[currentQuestion.correctAnswerIndex]}", Toast.LENGTH_LONG).show()
        }
        onAnswerSubmittedUpdate(true)
    } else {
        if (currentQuestionIndex < totalQuestions - 1) {
            onNextQuestion()
        } else {
            onShowResult()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(navController: NavController) {
    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(-1) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var showResult by rememberSaveable { mutableStateOf(false) }
    var answerSubmitted by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val currentQuestion = sampleQuestions.getOrNull(currentQuestionIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cybersecurity Test") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showResult) {
                TestResults(score = score, totalQuestions = sampleQuestions.size) {
                    currentQuestionIndex = 0
                    selectedOptionIndex = -1
                    score = 0
                    showResult = false
                    answerSubmitted = false
                }
            } else if (currentQuestion != null) {
                QuestionDisplay(
                    question = currentQuestion,
                    selectedOptionIndex = selectedOptionIndex,
                    answerSubmitted = answerSubmitted,
                    onOptionSelected = { if (!answerSubmitted) selectedOptionIndex = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        handleSubmitOrNext(
                            context = context,
                            selectedOptionIndex = selectedOptionIndex,
                            answerSubmitted = answerSubmitted,
                            currentQuestion = currentQuestion,
                            currentQuestionIndex = currentQuestionIndex,
                            totalQuestions = sampleQuestions.size,
                            score = score,
                            onScoreUpdate = { score = it },
                            onAnswerSubmittedUpdate = { answerSubmitted = it },
                            onNextQuestion = {
                                currentQuestionIndex++
                                selectedOptionIndex = -1
                                answerSubmitted = false
                            },
                            onShowResult = { showResult = true }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (answerSubmitted) "Next Question" else "Submit Answer")
                }
            } else {
                Text("Loading questions or test finished unexpectedly.")
            }
        }
    }
}

@Composable
fun QuestionDisplay(
    question: Question,
    selectedOptionIndex: Int,
    answerSubmitted: Boolean,
    onOptionSelected: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = question.text, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            question.options.forEachIndexed { index, option ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (selectedOptionIndex == index),
                            onClick = { onOptionSelected(index) },
                            role = Role.RadioButton,
                            enabled = !answerSubmitted
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedOptionIndex == index),
                        onClick = null,
                        enabled = !answerSubmitted,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = if (answerSubmitted && index == question.correctAnswerIndex) MaterialTheme.colorScheme.primary else if (answerSubmitted && selectedOptionIndex == index && index != question.correctAnswerIndex) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option, style = MaterialTheme.typography.bodyLarge)
                }
            }
            if (answerSubmitted) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explanation: ${question.explanation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TestResults(score: Int, totalQuestions: Int, onRetakeTest: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Test Finished!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your score: $score out of $totalQuestions", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetakeTest, modifier = Modifier.fillMaxWidth()) {
            Text("Retake Test")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestScreenPreview_Question() {
    MaterialTheme {
        TestScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun TestScreenPreview_Results() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
             TestResults(score = 3, totalQuestions = 4, onRetakeTest = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionDisplayPreview() {
    MaterialTheme {
        QuestionDisplay(
            question = sampleQuestions[0],
            selectedOptionIndex = 0,
            answerSubmitted = false,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionDisplayPreview_Submitted_Correct() {
    MaterialTheme {
        QuestionDisplay(
            question = sampleQuestions[0],
            selectedOptionIndex = 3,
            answerSubmitted = true,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionDisplayPreview_Submitted_Incorrect() {
    MaterialTheme {
        QuestionDisplay(
            question = sampleQuestions[0],
            selectedOptionIndex = 0,
            answerSubmitted = true,
            onOptionSelected = {}
        )
    }
}
