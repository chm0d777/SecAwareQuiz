package com.chmod777.secawarequiz.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chmod777.secawarequiz.HomeScreen
import com.chmod777.secawarequiz.MiniGameScreen
import com.chmod777.secawarequiz.ui.TestScreen
import com.chmod777.secawarequiz.ui.screens.auth.LoginScreen
import com.chmod777.secawarequiz.ui.screens.auth.RegistrationScreen

sealed class Screen(val route: String) {
    object Login : Screen(NavRoutes.LOGIN_SCREEN)
    object Register : Screen(NavRoutes.REGISTRATION_SCREEN)
    object Home : Screen(NavRoutes.HOME_SCREEN)
    object Quiz : Screen(NavRoutes.TEST_SCREEN) {
        fun createRoute(quizId: Int) = NavRoutes.testScreen(quizId)
    }
    object Minigame1 : Screen(NavRoutes.MINI_GAME_SCREEN)
    // object Minigame2 : Screen("minigame2") // Minigame2Screen.kt does not exist
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegistrationScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController, onSignOut = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }

        composable(
            route = Screen.Quiz.route, // Uses NavRoutes.TEST_SCREEN
            arguments = listOf(navArgument("quizId") { type = NavType.IntType })
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getInt("quizId")
            if (quizId != null) {
                TestScreen(navController = navController, questionId = quizId)
            } else {
                Text("Error: Missing quizId. Please go back.")
            }
        }

        composable(Screen.Minigame1.route) { // Uses NavRoutes.MINI_GAME_SCREEN
            MiniGameScreen(navController = navController)
        }

        // composable(Screen.Minigame2.route) {
        //     // Minigame2Screen()
        //     Text("Minigame 2")
        // }
    }
}
