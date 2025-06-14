package com.chmod777.secawarequiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.data.GameItem
import com.chmod777.secawarequiz.navigation.NavRoutes

val sampleGameItems = listOf(
    GameItem(
        id = 1,
        scenario = "Тема письма: Срочное подтверждение аккаунта!\n\nУважаемый клиент,\nМы обнаружили подозрительную активность в вашем аккаунте. Пожалуйста, нажмите здесь, чтобы подтвердить свои данные: [ссылка на sketchywebsite.com]",
        options = listOf(
            "Официальное обращение от компании ('Уважаемый клиент')",
            "Создаёт ощущение срочности",
            "Подозрительный URL-адрес",
            "Всё вышеперечисленное"
        ),
        correctOptionIndex = 3,
        explanation = "Все перечисленные элементы являются распространенными признаками фишинга: общие приветствия, срочные формулировки и подозрительные URL-адреса, часто не соответствующие предполагаемому отправителю."
    ),
    GameItem(
        id = 2,
        scenario = "SMS: 'Ваша посылка от Почты России ожидает доставки. Пожалуйста, обновите ваш адрес доставки здесь: [bit.ly/pochta-update123], чтобы избежать задержек. Требуется небольшая плата в размере 199 рублей.'",
        options = listOf(
            "Запрос небольшой платы за повторную доставку",
            "Использование сокращённой ссылки (bit.ly)",
            "Упоминание известной компании (Почта России)",
            "A и B"
        ),
        correctOptionIndex = 3,
        explanation = "Неожиданные запросы небольших платежей и использование сокращённых ссылок для сокрытия реального адреса часто встречаются в SMS-фишинге."
    ),
    GameItem(
        id = 3,
        scenario = "Email от: 'ozon-security@notification-center.com'\nТело письма: 'Ваш аккаунт был заблокирован из-за слишком большого количества неудачных попыток входа. Нажмите для разблокировки: [ozon.com.security-alert.net/unlock]'",
        options = listOf(
            "Адрес электронной почты похож на официальный.",
            "URL ссылки, хотя и содержит 'ozon.com', имеет дополнительные поддомены, что делает её подозрительной.",
            "Письмо сообщает о проблеме безопасности.",
            "Сценарий слишком распространён, чтобы быть фишинговой атакой."
        ),
        correctOptionIndex = 1,
        explanation = "Мошенники часто создают URL-адреса, которые сначала кажутся легитимными, но используют вводящие в заблуждение поддомены. 'ozon.com.security-alert.net' — это не настоящий домен Ozon."
    )
)

private fun handleGameAction(
    selectedOptionIndex: Int,
    answerSubmitted: Boolean,
    currentItem: GameItem,
    currentItemIndex: Int,
    totalItems: Int,
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    onAnswerSubmittedUpdate: (Boolean) -> Unit,
    onNextItem: () -> Unit,
    onShowResults: () -> Unit
) {
    if (!answerSubmitted) {
        if (selectedOptionIndex != -1) {
            if (selectedOptionIndex == currentItem.correctOptionIndex) {
                onScoreUpdate(score + 1)
            }
            onAnswerSubmittedUpdate(true)
        }
    } else {
        if (currentItemIndex < totalItems - 1) {
            onNextItem()
        } else {
            onShowResults()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGameScreen(navController: NavHostController) {
    var currentItemIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(-1) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var answerSubmitted by rememberSaveable { mutableStateOf(false) }
    var showResults by rememberSaveable { mutableStateOf(false) }

    val currentItem = sampleGameItems.getOrNull(currentItemIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Phishing Indicators Game") },
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
            if (showResults) {
                GameResults(score = score, totalItems = sampleGameItems.size) {
                    currentItemIndex = 0
                    selectedOptionIndex = -1
                    score = 0
                    answerSubmitted = false
                    showResults = false
                }
            } else if (currentItem != null) {
                GameScenarioDisplay(
                    item = currentItem,
                    selectedOptionIndex = selectedOptionIndex,
                    answerSubmitted = answerSubmitted,
                    onOptionSelected = { if (!answerSubmitted) selectedOptionIndex = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (answerSubmitted) {
                    Text(
                        text = currentItem.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        handleGameAction(
                            selectedOptionIndex = selectedOptionIndex,
                            answerSubmitted = answerSubmitted,
                            currentItem = currentItem,
                            currentItemIndex = currentItemIndex,
                            totalItems = sampleGameItems.size,
                            score = score,
                            onScoreUpdate = { score = it },
                            onAnswerSubmittedUpdate = { answerSubmitted = it },
                            onNextItem = {
                                currentItemIndex++
                                selectedOptionIndex = -1
                                answerSubmitted = false
                            },
                            onShowResults = { showResults = true }
                        )
                    },
                    enabled = selectedOptionIndex != -1 || answerSubmitted,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (answerSubmitted) "Следующий сценарий" else "Проверить ответ")
                }

            } else {
                Text("Загрузка игры завершилась неожиданно. Пожалуйста, попробуйте ещё раз.")
            }
        }
    }
}

@Composable
fun GameScenarioDisplay(
    item: GameItem,
    selectedOptionIndex: Int,
    answerSubmitted: Boolean,
    onOptionSelected: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Scenario:", style = MaterialTheme.typography.titleMedium)
            Text(item.scenario, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Which is the most prominent phishing indicator?", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            item.options.forEachIndexed { index, option ->
                val isCorrect = index == item.correctOptionIndex
                val isSelected = selectedOptionIndex == index

                val borderColor = when {
                    answerSubmitted && isCorrect -> Color.Green
                    answerSubmitted && isSelected && !isCorrect -> Color.Red
                    isSelected && !answerSubmitted -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                }
                val cardColors = CardDefaults.cardColors(
                    containerColor = if (answerSubmitted && isSelected) {
                        if (isCorrect) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !answerSubmitted) { onOptionSelected(index) },
                    border = BorderStroke(2.dp, borderColor),
                    colors = cardColors
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun GameResults(score: Int, totalItems: Int, onPlayAgain: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Мини игра завершена!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ваш счёт: $score из $totalItems корректных ответов.", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onPlayAgain, modifier = Modifier.fillMaxWidth()) {
            Text("Сыграть ещё раз")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MiniGameScreenPreview_Scenario() {
    MaterialTheme {
        MiniGameScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun MiniGameScreenPreview_Results() {
    MaterialTheme {
         GameResults(score = 2, totalItems = 3, onPlayAgain = {})
    }
}

@Preview(showBackground = true)
@Composable
fun GameScenarioDisplay_Preview() {
    MaterialTheme {
        GameScenarioDisplay(
            item = sampleGameItems[0],
            selectedOptionIndex = -1,
            answerSubmitted = false,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScenarioDisplay_Preview_Selected() {
    MaterialTheme {
        GameScenarioDisplay(
            item = sampleGameItems[0],
            selectedOptionIndex = 0,
            answerSubmitted = false,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScenarioDisplay_Preview_Submitted_Correct() {
    MaterialTheme {
        GameScenarioDisplay(
            item = sampleGameItems[0],
            selectedOptionIndex = sampleGameItems[0].correctOptionIndex,
            answerSubmitted = true,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScenarioDisplay_Preview_Submitted_Incorrect() {
    MaterialTheme {
        GameScenarioDisplay(
            item = sampleGameItems[0],
            selectedOptionIndex = if (sampleGameItems[0].correctOptionIndex == 0) 1 else 0,
            answerSubmitted = true,
            onOptionSelected = {}
        )
    }
}
