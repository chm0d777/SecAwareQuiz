package com.chmod777.secawarequiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.components.TestSelectionCard
import com.chmod777.secawarequiz.ui.theme.*
import com.chmod777.secawarequiz.viewmodels.home.HomeViewModel

@Composable
fun getMaterialIcon(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "security" -> Icons.Filled.Security
        "lock" -> Icons.Filled.Lock
        "school" -> Icons.Filled.School
        "dataset" -> Icons.Filled.Dataset
        "phishing" -> Icons.Filled.ReportProblem
        "password" -> Icons.Filled.Password
        "shield" -> Icons.Filled.Shield
        "lightbulb" -> Icons.Filled.Lightbulb
        else -> Icons.Filled.HelpOutline
    }
}

@Composable
fun getThemeColor(colorName: String): Color {
    return when (colorName) {
        "CardAccentPhishing" -> CardAccentPhishing
        "CardAccentPasswords" -> CardAccentPasswords
        "CardAccentLiteracy" -> CardAccentLiteracy
        "CardAccentDataSecurity" -> CardAccentDataSecurity
        else -> Color.Gray
    }
}

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    val homeViewModel: HomeViewModel = viewModel()
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle(lifecycleOwner = LocalLifecycleOwner.current)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (uiState.userName.isNullOrEmpty()) stringResource(R.string.home_welcome_message)
                           else stringResource(R.string.home_greeting_format, uiState.userName!!),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 26.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.home_instruction_subtitle),
                    style = MaterialTheme.typography.bodyLarge.copy(color = HomeSubtitleText, fontSize = 17.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        items(uiState.testCards) { cardItem ->
            val iconVector = getMaterialIcon(cardItem.iconName)
            val accentColorObject = getThemeColor(cardItem.accentColorName)

            TestSelectionCard(
                title = stringResource(id = cardItem.titleResId),
                icon = iconVector,
                accentColor = accentColorObject,
                contentDescription = stringResource(id = cardItem.contentDescResId),
                onClick = { navController.navigate(cardItem.route) }
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
             Spacer(modifier = Modifier.height(0.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SecAwareQuizTheme {
        HomeScreen(navController = rememberNavController(), modifier = Modifier.padding(16.dp))
    }
}
