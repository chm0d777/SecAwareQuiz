package com.chmod777.secawarequiz.ui.screens.quiz


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.components.ResultScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultsScreen(
    navController: NavHostController,
    score: Int,
    totalQuestions: Int
) {
    val userName = "Пользователь"
    val scorePercent = if (totalQuestions > 0) {
        (score.toFloat() / totalQuestions.toFloat() * 100).toInt()
    } else {
        0
    }
    val incorrectAnswers = totalQuestions - score


    ResultScreen(
        userName = userName,
        scorePercent = scorePercent,
        correctAnswers = score,
        incorrectAnswers = incorrectAnswers,
        onReviewClicked = {
            navController.navigate(Screen.QuizReview.route)
        },
        onContinueClicked = {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }

    )
}
