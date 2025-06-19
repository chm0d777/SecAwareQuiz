package com.chmod777.secawarequiz.ui.screens.minigames

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
import androidx.compose.foundation.lazy.LazyColumn
// import androidx.compose.foundation.lazy.items // Not directly used, item {} scope is used
import androidx.compose.foundation.shape.RoundedCornerShape // Added import
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults // Added import
// import androidx.compose.material3.Card // Not used directly in this screen's buttons
// import androidx.compose.material3.CardDefaults // Not used directly
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
// import androidx.compose.material3.surfaceColorAtElevation // Not strictly needed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Added import
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Added import
// import androidx.lifecycle.viewmodel.compose.viewModel // Not used
import androidx.compose.foundation.background // Added for Box background
import androidx.navigation.NavController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.viewmodels.minigames.FakeLoginViewModel

// Figma Color Definitions
private val figmaButtonBlue = Color(0xFF2196F3) // This could be replaced by MaterialTheme.colorScheme.primary
private val figmaButtonTextWhite = Color(0xFFFFFFFF) // This could be replaced by MaterialTheme.colorScheme.onPrimary

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
    val showResults by viewModel.showResults.collectAsState() // This is the state to use
    // val gameItemsCount = viewModel.gameItems.collectAsState().value.size // Replaced by actualTotalForNavigation
    val actualTotalForNavigation by viewModel.actualTotalItemsForResults.collectAsState()

    LaunchedEffect(showResults) { // Use the specific state for LaunchedEffect
        if (showResults) {
            navController.navigate(Screen.FakeLoginResults.createRoute(score, actualTotalForNavigation)) {
                popUpTo(Screen.Home.route)
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        if (showResults) { // Conditional rendering for the entire Scaffold content
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
            // Existing Box with if (isLoading) / else if (currentItem == null) / else (LazyColumn) etc.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .background(MaterialTheme.colorScheme.background), // Ensure background is applied
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else if (currentItem == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.game_no_data), color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadGameItems() },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) {
                            Text(stringResource(R.string.common_try_again), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                } else {
                    val item = currentItem!!
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.common_score_prefix) + "$score",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Text(
                                text = item.serviceName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        if (!userAnswered) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                                ) {
                                    Button(
                                        onClick = { viewModel.selectAnswer(userChoseFake = false) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                                        modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                    ) {
                                        Text(stringResource(R.string.real_button), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    Button(
                                        onClick = { viewModel.selectAnswer(userChoseFake = true) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                                        modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                    ) {
                                        Text(stringResource(R.string.fake_button), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
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
                                        color = if (isCorrect == true) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                    Text(
                                        text = item.explanation,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    if (item.isFake && item.elementsToSpot.isNotEmpty()) {
                                        Text(
                                            text = stringResource(R.string.elements_to_spot_label),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                        )
                                        item.elementsToSpot.forEach { element ->
                                            Text(
                                                " - $element",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(
                                        onClick = { viewModel.nextItem() },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                                    ) {
                                        Text(stringResource(R.string.next_button), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
