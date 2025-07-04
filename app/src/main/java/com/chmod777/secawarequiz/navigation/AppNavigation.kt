package com.chmod777.secawarequiz.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chmod777.secawarequiz.HomeScreen
import com.chmod777.secawarequiz.MiniGameScreen
import com.chmod777.secawarequiz.data.AppDatabase
import com.chmod777.secawarequiz.ui.TestScreen
import com.chmod777.secawarequiz.ui.TestViewModel
import com.chmod777.secawarequiz.viewmodels.minigames.PhishingGameViewModel
import com.chmod777.secawarequiz.ui.screens.auth.LoginScreen
import com.chmod777.secawarequiz.ui.screens.auth.RegistrationScreen
import com.chmod777.secawarequiz.ui.screens.minigames.FakeLoginScreen
import com.chmod777.secawarequiz.ui.screens.minigames.MiniGameResultsScreen
import com.chmod777.secawarequiz.ui.screens.profile.ProfileScreen
import com.chmod777.secawarequiz.ui.screens.quiz.QuizResultsScreen
import androidx.annotation.StringRes
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.ui.screens.AllTestsScreen
import com.chmod777.secawarequiz.ui.screens.auth.PasswordResetScreen
import com.chmod777.secawarequiz.ui.screens.profile.AboutAppScreen
import com.chmod777.secawarequiz.ui.screens.profile.ChangePasswordScreen
import com.chmod777.secawarequiz.ui.screens.profile.EditProfileScreen
import com.chmod777.secawarequiz.ui.screens.profile.NotificationsSettingsScreen
import com.chmod777.secawarequiz.ui.screens.quiz.ReviewScreen
import com.chmod777.secawarequiz.ui.screens.minigames.GameReviewScreen
import com.chmod777.secawarequiz.ui.screens.minigames.FakeLoginResultsScreen
import com.chmod777.secawarequiz.ui.screens.minigames.FakeLoginReviewScreen
import com.chmod777.secawarequiz.viewmodels.minigames.FakeLoginViewModel



sealed class Screen(val route: String, @StringRes val titleResId: Int? = null) {
    object Login : Screen(NavRoutes.LOGIN_SCREEN, R.string.login_button_text)
    object Register : Screen(NavRoutes.REGISTRATION_SCREEN, R.string.register_button_text)
    object Home : Screen(NavRoutes.HOME_SCREEN, R.string.app_title_full)
    object Profile : Screen(NavRoutes.PROFILE_SCREEN, R.string.profile_screen_title)
    object Quiz : Screen(NavRoutes.QUIZ_SCREEN_ROUTE_TEMPLATE) {
        fun createRoute(questionId: Int) = "test/$questionId"
    }
    object QuizResults : Screen(NavRoutes.QUIZ_RESULTS_SCREEN_ROUTE_TEMPLATE, R.string.quiz_results_screen_title) {
        fun createRoute(score: Int, totalQuestions: Int) = "quiz_results/$score/$totalQuestions"
    }
    object Minigame1 : Screen(NavRoutes.MINI_GAME_SCREEN, R.string.minigame1_screen_title)
    object MiniGameResults : Screen(NavRoutes.MINI_GAME_RESULTS_SCREEN_ROUTE_TEMPLATE, R.string.minigame_results_screen_title) {
        fun createRoute(score: Int, totalItems: Int) = "mini_game_results/$score/$totalItems"
    }
    object FakeLoginGame : Screen(NavRoutes.FAKE_LOGIN_GAME_SCREEN, R.string.fake_login_game_title_short)
    object FakeLoginResults : Screen(NavRoutes.FAKE_LOGIN_RESULTS_SCREEN_ROUTE_TEMPLATE, R.string.fake_login_results_title) {
        fun createRoute(score: Int, totalItems: Int) = "fake_login_results/$score/$totalItems"
    }
    object PasswordReset : Screen(NavRoutes.PASSWORD_RESET_SCREEN, R.string.password_reset_title)
    object AllTests : Screen(NavRoutes.ALL_TESTS_SCREEN, R.string.all_tests_screen_title)
    object QuizReview : Screen(NavRoutes.QUIZ_REVIEW_SCREEN, R.string.review_answers_screen_title)
    object GameReview : Screen(NavRoutes.GAME_REVIEW_SCREEN, R.string.review_answers_screen_title)
    object FakeLoginReview : Screen(NavRoutes.FAKE_LOGIN_REVIEW_SCREEN, R.string.fake_login_review_title)

    object EditProfile : Screen(NavRoutes.EDIT_PROFILE_SCREEN, R.string.edit_profile_title)
    object ChangePassword : Screen(NavRoutes.CHANGE_PASSWORD_SCREEN, R.string.change_password_title)
    object NotificationsSettings : Screen(NavRoutes.NOTIFICATIONS_SETTINGS_SCREEN, R.string.notifications_settings_title)
    object AboutApp : Screen(NavRoutes.ABOUT_APP_SCREEN, R.string.about_app_title)
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
        modifier = modifier.background(Color.White)
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegistrationScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("questionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getInt("questionId")
            if (quizId != null) {
                val context = LocalContext.current
                val questionDao = AppDatabase.getDatabase(context).questionDao()
                val testViewModel: TestViewModel = viewModel(
                    factory = TestViewModel.provideFactory(questionDao)
                )
                TestScreen(
                    navController = navController,
                    testViewModel = testViewModel,
                    internalQuizId = quizId
                )
            } else {
                Text("Ошибка: Отсутствует questionId. Пожалуйста, вернитесь назад.")
            }
        }

        composable(Screen.Minigame1.route) {
            val context = LocalContext.current
            val gameItemDao = AppDatabase.getDatabase(context).gameItemDao()
            val phishingGameViewModel: PhishingGameViewModel = viewModel(
                factory = PhishingGameViewModel.provideFactory(gameItemDao)
            )
            MiniGameScreen(navController = navController, viewModel = phishingGameViewModel)
        }

        composable(
            route = Screen.QuizResults.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("totalQuestions") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 0
            QuizResultsScreen(
                navController = navController,
                score = score,
                totalQuestions = totalQuestions
            )
        }

        composable(
            route = Screen.MiniGameResults.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("totalItems") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val totalItems = backStackEntry.arguments?.getInt("totalItems") ?: 0
            MiniGameResultsScreen(
                navController = navController,
                score = score,
                totalItems = totalItems
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }

        composable(Screen.FakeLoginGame.route) {
            val context = LocalContext.current
            val fakeLoginDao = AppDatabase.getDatabase(context).fakeLoginGameItemDao()
            val fakeLoginViewModel: FakeLoginViewModel = viewModel(
                factory = FakeLoginViewModel.provideFactory(fakeLoginDao)
            )
            FakeLoginScreen(navController = navController, viewModel = fakeLoginViewModel)
        }

        composable(Screen.PasswordReset.route) {
            PasswordResetScreen(navController = navController)
        }

        composable(Screen.AllTests.route) {
            AllTestsScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }

        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }

        composable(Screen.NotificationsSettings.route) {
            NotificationsSettingsScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }

        composable(Screen.AboutApp.route) {
            AboutAppScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }
        composable(Screen.QuizReview.route) {
            ReviewScreen(navController = navController)
        }
        composable(Screen.GameReview.route) {
            GameReviewScreen(navController = navController)
        }
        composable(Screen.FakeLoginResults.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("totalItems") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val totalItems = backStackEntry.arguments?.getInt("totalItems") ?: 0
            FakeLoginResultsScreen(
                navController = navController,
                score = score,
                totalItems = totalItems
            )
        }
        composable(Screen.FakeLoginReview.route) {
            FakeLoginReviewScreen(navController = navController)
        }
    }
}
