package com.chmod777.secawarequiz.ui.screens.minigames

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.data.model.AnsweredGameItemDetails
import com.chmod777.secawarequiz.viewmodels.GameReviewViewModel // Changed import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameReviewScreen( // Renamed screen
    navController: NavHostController,
    reviewViewModel: GameReviewViewModel = viewModel() // Changed ViewModel type
) {
    val answeredGameItems by reviewViewModel.answeredGameItems.collectAsState() // Changed state variable

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.review_answers_screen_title)) }, // Can reuse or make a new string
                navigationIcon = {
                    IconButton(onClick = {
                        reviewViewModel.clearReviewData()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface, // Use direct surface color
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        if (answeredGameItems.isEmpty()) { // Changed variable
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
                    .background(MaterialTheme.colorScheme.background), // Added background
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.review_answers_no_data),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background) // Added background
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(answeredGameItems) { index, item -> // Changed variable
                    GameAnswerReviewItem(item = item, questionNumber = index + 1) // Changed composable
                    if (index < answeredGameItems.size - 1) { // Changed variable
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun GameAnswerReviewItem(item: AnsweredGameItemDetails, questionNumber: Int) { // Changed parameter type
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.review_answers_question_number, questionNumber), // Reusing string
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Adapted for GameItem: item.gameItem.scenario
            Text(item.gameItem.scenario, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                stringResource(R.string.review_answers_your_answer_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Adapted for GameItem: item.gameItem.options
            val userAnswerText = item.gameItem.options.getOrElse(item.userAnswerIndex) { stringResource(R.string.review_answers_not_answered) }
            Text(
                text = userAnswerText,
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.wasCorrect) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (!item.wasCorrect) {
                Text(
                    stringResource(R.string.review_answers_correct_answer_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Adapted for GameItem: item.gameItem.options & item.gameItem.correctOptionIndex
                Text(
                    text = item.gameItem.options[item.gameItem.correctOptionIndex],
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                stringResource(R.string.review_answers_explanation_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Adapted for GameItem: item.gameItem.explanation
            Text(
                text = item.gameItem.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
