package com.chmod777.secawarequiz.ui.screens.minigames

// Removed local ResultScreen imports
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.components.ResultScreen // Import common ResultScreen
// Keep other necessary imports for MiniGameResultsScreen itself, if any.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniGameResultsScreen(
    navController: NavHostController,
    score: Int,
    totalItems: Int
) {
    val userName = "Пользователь" // Placeholder or from ViewModel
    val scorePercent = if (totalItems > 0) {
        (score.toFloat() / totalItems.toFloat() * 100).toInt()
    } else {
        0
    }
    val incorrectAnswers = totalItems - score

    // Call the common ResultScreen
    ResultScreen(
        userName = userName,
        scorePercent = scorePercent,
        correctAnswers = score,
        incorrectAnswers = incorrectAnswers,
        onReviewClicked = {
            navController.navigate(Screen.GameReview.route)
        },
        onContinueClicked = {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }
        // If the common ResultScreen had a subtitle parameter, it would be passed here.
        // The common version currently has a generic subtitle.
    )
}
