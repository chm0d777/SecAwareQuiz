package com.chmod777.secawarequiz.ui.screens.minigames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Minigame2Screen(
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.minigame2_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { }) {
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
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(R.string.minigame2_placeholder_text), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Minigame2ScreenPreview() {
    SecAwareQuizTheme {
        Minigame2Screen()
    }
}
