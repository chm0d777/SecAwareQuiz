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
        text = "Что является распространенным признаком фишингового письма?",
        options = listOf(
            "Срочный запрос личной информации",
            "Обычное приветствие типа 'Уважаемый клиент'",
            "Орфографические и грамматические ошибки",
            "Все вышеперечисленное".
        ),
        correctAnswerIndex = 3,
        explanation = "Фишинговые электронные письма часто пытаются создать ощущение срочности, используют общие приветствия и содержат ошибки."
    ),
    Question(
        id = 2,
        text = "Какой из перечисленных ниже паролей является наиболее надежным?",
        options = listOf(
            "password123",
            "MyBirthDay1990!",
            "Tr0ub4dor&3",
            "qwertyuiop"
        ),
        correctAnswerIndex = 2,
        explanation = "'Tr0ub4dor&3' самый надежный, так как содержит различные символы и сочетания букв и цифр."
    ),
    Question(
        id = 3,
        text = "Что означает HTTPS в URL-адресе веб-сайта?",
        options = listOf(
            "Веб-сайт на 100% защищен от всех угроз.",
            "Веб-сайт имеет быстрое соединение.",
            "Соединение между вашим браузером и веб-сайтом зашифровано.",
            "Веб-сайт размещен в Соединенных Штатах."
        ),
        correctAnswerIndex = 2,
        explanation = "HTTPS означает, что данные, передаваемые между вашим браузером и веб-сайтом, зашифрованы, что делает их более безопасными, чем HTTP."
    ),
    Question(
        id = 4,
        text = "Что вам следует делать, если вы получили подозрительное электронное письмо с запросом ваших банковских реквизитов?",
        options = listOf(
            "Немедленно сообщить свои данные.",
            "Кликнуть по ссылке чтобы посмотреть что там.",
            "Удалить письмо и не отвечать на него.",
            "Позвонить по номеру телефона, указанному в электронном письме, для подтверждения."
        ),
        correctAnswerIndex = 2,
        explanation = "Никогда не сообщайте личные данные по электронной почте и не переходите по подозрительным ссылкам. Если вас это беспокоит, свяжитесь со своим банком напрямую через его официальный веб-сайт или по номеру телефона."
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
        Toast.makeText(context, "Выберите нужный вариант.", Toast.LENGTH_SHORT).show()
        return
    }

    if (!answerSubmitted) {
        if (selectedOptionIndex == currentQuestion.correctAnswerIndex) {
            onScoreUpdate(score + 1)
            Toast.makeText(context, "Правильно!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Неправильный. Правильным ответом было: ${currentQuestion.options[currentQuestion.correctAnswerIndex]}", Toast.LENGTH_LONG).show()
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
                title = { Text("Тест по безопасности в интернете") },
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
                    Text(if (answerSubmitted) "Следующий вопрос" else "Подтвердить ответ")
                }
            } else {
                Text("Загрузка вопросов или тестов закончилась с ошибкой.")
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
                    text = "Объяснение:: ${question.explanation}",
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
        Text("Тест завершён!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ваш результат: $score из $totalQuestions", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetakeTest, modifier = Modifier.fillMaxWidth()) {
            Text("Пройти заново")
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
