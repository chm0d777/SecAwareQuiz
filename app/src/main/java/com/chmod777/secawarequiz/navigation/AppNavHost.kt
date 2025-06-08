package com.chmod777.secawarequiz.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chmod777.secawarequiz.data.QuestionDao
import com.chmod777.secawarequiz.ui.MainScreen
import com.chmod777.secawarequiz.ui.TestScreen
import com.chmod777.secawarequiz.ui.TestViewModel

object Screen {
    const val MAIN_SCREEN = "main_screen"
    const val TEST_SCREEN = "test_screen/{questionId}"
    fun testScreenRoute(questionId: Int) = "test_screen/$questionId"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    questionDao: QuestionDao
) {
    val testViewModel: TestViewModel =
        viewModel(factory = TestViewModel.provideFactory(questionDao))

    NavHost(navController = navController, startDestination = Screen.MAIN_SCREEN) {
        composable(Screen.MAIN_SCREEN) {
            MainScreen(
                onTestSelected = { questionId ->
                    navController.navigate(Screen.testScreenRoute(questionId))
                }
            )
        }

        composable(
            route = Screen.TEST_SCREEN,
            arguments = listOf(navArgument("questionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getInt("questionId") ?: 1
            TestScreen(
                navController = navController,
                testViewModel = testViewModel,
                questionId = questionId
            )
        }
    }
}