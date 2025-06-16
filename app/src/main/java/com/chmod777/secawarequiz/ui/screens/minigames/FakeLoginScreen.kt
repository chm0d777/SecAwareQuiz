package com.chmod777.secawarequiz.ui.screens.minigames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.viewmodels.minigames.FakeLoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FakeLoginScreen(
    navController: NavController,
    viewModel: FakeLoginViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val currentItem by viewModel.currentItem.collectAsState()
    val score by viewModel.score.collectAsState()
    val userAnswered by viewModel.userAnswered.collectAsState()
    val isCorrect by viewModel.isCorrect.collectAsState()
    val showResults by viewModel.showResults.collectAsState()
    val gameItemsCount = viewModel.gameItems.collectAsState().value.size

    LaunchedEffect(showResults) {
        if (showResults) {
            navController.navigate(Screen.MiniGameResults.createRoute(score, gameItemsCount)) {
                popUpTo(Screen.Home.route) // popUpTo(Screen.FakeLoginGame.route) { inclusive = true }
            }
            viewModel.onResultsNavigated()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = Screen.FakeLoginGame.titleResId ?: R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (currentItem == null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.game_no_data))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadGameItems() }) {
                        Text(stringResource(R.string.common_try_again))
                    }
                }
            } else {
                val item = currentItem!!
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    item {
                        Text(
                            text = stringResource(R.string.common_score_prefix) + "$score",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = item.serviceName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (!userAnswered) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(onClick = { viewModel.selectAnswer(userChoseFake = false) }) {
                                    Text(stringResource(R.string.real_button))
                                }
                                Button(onClick = { viewModel.selectAnswer(userChoseFake = true) }) {
                                    Text(stringResource(R.string.fake_button))
                                }
                            }
                        }
                    }

                    if (userAnswered) {
                        item {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (isCorrect == true) stringResource(R.string.correct_answer_feedback) else stringResource(R.string.incorrect_answer_feedback),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = if (isCorrect == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                                Text(
                                    text = item.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                if (item.isFake && item.elementsToSpot.isNotEmpty()) {
                                    Text(
                                        text = stringResource(R.string.elements_to_spot_label),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                    )
                                    item.elementsToSpot.forEach { element ->
                                        Text(" - $element", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(onClick = { viewModel.nextItem() }) {
                                    Text(stringResource(R.string.next_button))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
