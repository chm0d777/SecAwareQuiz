package com.chmod777.secawarequiz.ui.screens.minigames

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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGameResultsScreen(
    navController: NavHostController,
    score: Int,
    totalItems: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.minigame_results_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } } }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_to_home_screen))
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
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.minigame_results_game_over),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.size(150.dp)) {}
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.minigame_results_your_score),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "$score / $totalItems",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.Minigame1.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(stringResource(R.string.common_play_again))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(stringResource(R.string.common_to_home_screen))
            }
        }
    }
}
