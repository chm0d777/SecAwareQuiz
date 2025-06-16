package com.chmod777.secawarequiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.data.GameItem
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.viewmodels.minigames.PhishingGameViewModel

private val previewSampleGameItems = listOf(
    GameItem(
        id = 1,
        scenario = "Тема письма: Срочное подтверждение аккаунта!...",
        options = listOf("Вариант 1", "Вариант 2", "Вариант 3", "Всё вышеперечисленное"),
        correctOptionIndex = 3,
        explanation = "Объяснение для предварительного просмотра..."
    ),
    GameItem(
        id = 2,
        scenario = "Вы получили SMS с неизвестного номера о выигрыше...",
        options = listOf("Перейти по ссылке", "Удалить SMS", "Позвонить на номер"),
        correctOptionIndex = 1,
        explanation = "Не переходите по ссылкам и не звоните на неизвестные номера из SMS о выигрышах."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGameScreen(
    navController: NavHostController,
    viewModel: PhishingGameViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentItemFromVM by viewModel.currentItem.collectAsState()
    val selectedOptionIndexFromVM by viewModel.selectedOptionIndex.collectAsState()
    val scoreFromVM by viewModel.score.collectAsState()
    val answerSubmittedFromVM by viewModel.answerSubmitted.collectAsState()
    val showResultsFromVM by viewModel.showResults.collectAsState()
    val gameItemsFromVM = viewModel.gameItems.collectAsState().value

    LaunchedEffect(showResultsFromVM) {
        if (showResultsFromVM) {
            val totalItems = gameItemsFromVM.size
            navController.navigate(Screen.MiniGameResults.createRoute(scoreFromVM, totalItems)) {
                popUpTo(Screen.Home.route)
            }
            viewModel.onResultsNavigated()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_card_phishing_game_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
                Text(
                    text = stringResource(R.string.minigame_loading_game),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else if (error != null) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = stringResource(R.string.common_error),
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    stringResource(R.string.common_error_prefix) + error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )
                Button(onClick = { viewModel.loadGameItems() }) {
                    Text(stringResource(R.string.common_try_again))
                }
            } else if (showResultsFromVM) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
                Text(
                    text = stringResource(R.string.minigame_preparing_results),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else if (currentItemFromVM != null) {
                val item = currentItemFromVM!!
                GameScenarioDisplay(
                    item = item,
                    selectedOptionIndex = selectedOptionIndexFromVM ?: -1,
                    answerSubmitted = answerSubmittedFromVM,
                    isCorrect = if (answerSubmittedFromVM) selectedOptionIndexFromVM == item.correctOptionIndex else null,
                    onOptionSelected = { index -> viewModel.selectOption(index) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (answerSubmittedFromVM) {
                            viewModel.nextItem()
                        } else {
                            viewModel.submitAnswer()
                        }
                    },
                    enabled = selectedOptionIndexFromVM != null || answerSubmittedFromVM,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (answerSubmittedFromVM) stringResource(R.string.minigame_next_scenario) else stringResource(R.string.minigame_check_answer))
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.common_score_prefix) + scoreFromVM,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

            } else {
                Text(stringResource(R.string.minigame_no_data_available))
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.loadGameItems() }) {
                    Text(stringResource(R.string.minigame_load_game_button))
                }
            }
        }
    }
}

@Composable
fun GameScenarioDisplay(
    item: GameItem,
    selectedOptionIndex: Int,
    answerSubmitted: Boolean,
    isCorrect: Boolean?,
    onOptionSelected: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.minigame_scenario_label), style = MaterialTheme.typography.titleLarge)
            Text(item.scenario, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 16.dp))
            Text(stringResource(R.string.minigame_select_option_label), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            item.options.forEachIndexed { index, option ->
                val isSelected = selectedOptionIndex == index

                val borderColor = when {
                    answerSubmitted && isCorrect == true && isSelected -> MaterialTheme.colorScheme.tertiary
                    answerSubmitted && isCorrect == false && isSelected -> MaterialTheme.colorScheme.error
                    isSelected && !answerSubmitted -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                }
                val containerColor = when {
                    answerSubmitted && isCorrect == true && isSelected -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                    answerSubmitted && isCorrect == false && isSelected -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    isSelected && !answerSubmitted -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                val textColor = when {
                     answerSubmitted && isCorrect == true && isSelected -> MaterialTheme.colorScheme.onTertiaryContainer
                     answerSubmitted && isCorrect == false && isSelected -> MaterialTheme.colorScheme.onErrorContainer
                     isSelected && !answerSubmitted -> MaterialTheme.colorScheme.onPrimaryContainer
                     else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !answerSubmitted) { onOptionSelected(index) },
                    border = BorderStroke(2.dp, borderColor),
                    colors = CardDefaults.cardColors(containerColor = containerColor)
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
            }

            if (answerSubmitted) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.size(70.dp).align(Alignment.CenterHorizontally)) {
                     Icon(
                         imageVector = if (isCorrect == true) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                         contentDescription = if (isCorrect == true) stringResource(R.string.common_correct) else stringResource(R.string.common_incorrect),
                         modifier = Modifier.fillMaxSize(),
                         tint = if (isCorrect == true) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                     )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun GameResults(score: Int, totalItems: Int, onPlayAgain: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(150.dp)) { }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Мини игра завершена!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ваш счёт: $score из $totalItems.", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onPlayAgain,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Сыграть ещё раз", style = MaterialTheme.typography.labelLarge)
        }
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
            item = previewSampleGameItems[0],
            selectedOptionIndex = -1,
            answerSubmitted = false,
            isCorrect = null,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScenarioDisplay_Preview_Selected() {
    MaterialTheme {
        GameScenarioDisplay(
            item = previewSampleGameItems[0],
            selectedOptionIndex = 0,
            answerSubmitted = false,
            isCorrect = null,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScenarioDisplay_Preview_Submitted_Correct() {
    MaterialTheme {
        GameScenarioDisplay(
            item = previewSampleGameItems[0],
            selectedOptionIndex = previewSampleGameItems[0].correctOptionIndex,
            answerSubmitted = true,
            isCorrect = true,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScenarioDisplay_Preview_Submitted_Incorrect() {
    MaterialTheme {
        GameScenarioDisplay(
            item = previewSampleGameItems[0],
            selectedOptionIndex = if (previewSampleGameItems[0].correctOptionIndex == 0) 1 else 0,
            answerSubmitted = true,
            isCorrect = false,
            onOptionSelected = {}
        )
    }
}
