package com.chmod777.secawarequiz.ui.screens.minigames


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.components.ResultScreen // Import common ResultScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FakeLoginResultsScreen(
    navController: NavHostController,
    score: Int,
    totalItems: Int
) {
    val userName = "Пользователь"
    val scorePercent = if (totalItems > 0) {
        (score.toFloat() / totalItems.toFloat() * 100).toInt()
    } else {
        0
    }
    val incorrectAnswers = totalItems - score


    ResultScreen(
        userName = userName,
        scorePercent = scorePercent,
        correctAnswers = score,
        incorrectAnswers = incorrectAnswers,
        onReviewClicked = {
            navController.navigate(Screen.FakeLoginReview.route)
        },
        onContinueClicked = {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }

    )
}
