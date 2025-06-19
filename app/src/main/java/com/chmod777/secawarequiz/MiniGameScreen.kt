package com.chmod777.secawarequiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TopAppBarDefaults
// import androidx.compose.material3.surfaceColorAtElevation // Not strictly needed if surface is used directly
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Already present, good.
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme
import com.chmod777.secawarequiz.ui.theme.LightBlueNavIndicator // Added import
import com.chmod777.secawarequiz.data.GameItem
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.viewmodels.minigames.PhishingGameViewModel


private val figmaOptionCardBg = Color(0xFFF5F5F5)
private val figmaOptionCorrectBg = Color(0xFF4CAF50)
private val figmaOptionIncorrectBg = Color(0xFFF44336)


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
    val showResultsFromVM by viewModel.showResults.collectAsState() // This is the state to use
    val gameItemsFromVM = viewModel.gameItems.collectAsState().value // Needed for totalItems in navigation

    LaunchedEffect(showResultsFromVM) { // Use the specific state for LaunchedEffect
        if (showResultsFromVM) {
            val totalItems = gameItemsFromVM.size // This might be 0 if list is cleared, handle in createRoute if needed
            navController.navigate(Screen.MiniGameResults.createRoute(scoreFromVM, totalItems.coerceAtLeast(1))) {
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        if (showResultsFromVM) { // Conditional rendering for the entire Scaffold content
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Переход к результатам...", // Hardcoded string
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp
                    )
                }
            }
        } else {
            // Existing Column with if (isLoading) / else if (error) / else if (currentItemFromVM) etc.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (isLoading) {
                    Spacer(modifier = Modifier.weight(0.3f))
                    CircularProgressIndicator(modifier = Modifier.size(64.dp), color = MaterialTheme.colorScheme.primary)
                    Text(
                        text = stringResource(R.string.minigame_loading_game),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(0.7f))
                } else if (error != null) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
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
                        Button(
                            onClick = { viewModel.loadGameItems() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightBlueNavIndicator,
                                contentColor = Color.White
                            )
                        ) {
                            Text(stringResource(R.string.common_try_again))
                        }
                    }
                } else if (currentItemFromVM != null) { // Removed showResultsFromVM check here as it's handled above
                    val item = currentItemFromVM!!
                    Text(
                        text = stringResource(R.string.common_score_prefix) + scoreFromVM,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                        GameScenarioDisplay(
                            item = item,
                            selectedOptionIndex = selectedOptionIndexFromVM ?: -1,
                            answerSubmitted = answerSubmittedFromVM,
                            isCorrect = if (answerSubmittedFromVM) selectedOptionIndexFromVM == item.correctOptionIndex else null,
                            onOptionSelected = { index -> viewModel.selectOption(index) }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (answerSubmittedFromVM) {
                                viewModel.nextItem()
                            } else {
                                viewModel.submitAnswer()
                            }
                        },
                        enabled = selectedOptionIndexFromVM != null || answerSubmittedFromVM,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightBlueNavIndicator,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            if (answerSubmittedFromVM) stringResource(R.string.minigame_next_scenario) else stringResource(R.string.minigame_check_answer),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                     Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.minigame_no_data_available), color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadGameItems() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightBlueNavIndicator,
                                contentColor = Color.White
                            )
                        ) {
                            Text(stringResource(R.string.minigame_load_game_button))
                        }
                    }
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
    Column {
        Text(
            stringResource(R.string.minigame_scenario_label),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            item.scenario,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            stringResource(R.string.minigame_select_option_label),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(12.dp))

        item.options.forEachIndexed { index, option ->
            val isCurrentlySelected = selectedOptionIndex == index
            val isCorrectOption = index == item.correctOptionIndex

            val optionCardBackgroundColor = when {
                answerSubmitted && isCorrectOption && isCorrect == true -> figmaOptionCorrectBg
                answerSubmitted && isCorrectOption && isCorrect == false -> figmaOptionCorrectBg
                answerSubmitted && isCurrentlySelected && isCorrect == false -> figmaOptionIncorrectBg
                !answerSubmitted && isCurrentlySelected -> MaterialTheme.colorScheme.primaryContainer
                else -> figmaOptionCardBg
            }
            val optionTextColor = when {
                answerSubmitted && (isCorrectOption || (isCurrentlySelected && isCorrect == false)) -> Color.White
                !answerSubmitted && isCurrentlySelected -> MaterialTheme.colorScheme.onPrimaryContainer
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            val optionBorderColor = if (!answerSubmitted && isCurrentlySelected) MaterialTheme.colorScheme.primary else Color.Transparent

            Card(
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable(enabled = !answerSubmitted) { onOptionSelected(index) },
                border = BorderStroke(if (optionBorderColor != Color.Transparent) 2.dp else 0.dp, optionBorderColor),
                colors = CardDefaults.cardColors(containerColor = optionCardBackgroundColor)
            ) {
                Row(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!answerSubmitted && isCurrentlySelected) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    } else if (answerSubmitted && isCurrentlySelected) {
                        Icon(
                            imageVector = if (isCorrect == true) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                            contentDescription = if (isCorrect == true) "Correct" else "Incorrect",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    } else if (answerSubmitted && isCorrectOption) {
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
                    Text(text = option, style = MaterialTheme.typography.bodyMedium, color = optionTextColor, modifier = Modifier.weight(1f))
                }
            }
        }

        if (answerSubmitted) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                     Icon(
                        imageVector = if (isCorrect == true) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                        contentDescription = if (isCorrect == true) stringResource(R.string.common_correct) else stringResource(R.string.common_incorrect),
                        modifier = Modifier.size(40.dp),
                        tint = if (isCorrect == true) figmaOptionCorrectBg else figmaOptionIncorrectBg
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
    SecAwareQuizTheme {
         GameResults(score = 2, totalItems = 3, onPlayAgain = {})
    }
}

@Preview(showBackground = true)
@Composable
fun GameScenarioDisplay_Preview() {
    SecAwareQuizTheme {
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
    SecAwareQuizTheme {
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
    SecAwareQuizTheme {
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
    SecAwareQuizTheme {
        GameScenarioDisplay(
            item = previewSampleGameItems[0],
            selectedOptionIndex = if (previewSampleGameItems[0].correctOptionIndex == 0) 1 else 0,
            answerSubmitted = true,
            isCorrect = false,
            onOptionSelected = {}
        )
    }
}
