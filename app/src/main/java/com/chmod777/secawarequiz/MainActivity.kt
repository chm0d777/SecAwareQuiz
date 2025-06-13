package com.chmod777.secawarequiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

object NavRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val TEST_SCREEN = "test_screen"
    const val MINI_GAME_SCREEN = "mini_game_screen"
}

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val initialUser = firebaseAuth.currentUser

        val startDestination = if (initialUser != null) NavRoutes.HOME else NavRoutes.LOGIN

        LaunchedEffect(firebaseAuth.currentUser) {
            val currentUser = firebaseAuth.currentUser
            val currentRoute = navController.currentBackStackEntry?.destination?.route

            if (currentUser == null) {
                if (currentRoute != NavRoutes.LOGIN && currentRoute != NavRoutes.REGISTER) {
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                if (currentRoute == NavRoutes.LOGIN || currentRoute == NavRoutes.REGISTER) {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(currentRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (currentRoute == null && startDestination == NavRoutes.LOGIN) {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }

        NavHost(navController = navController, startDestination = startDestination) {
            composable(NavRoutes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {  },
                    onNavigateToRegister = { navController.navigate(NavRoutes.REGISTER) }
                )
            }
            composable(NavRoutes.REGISTER) {
                RegistrationScreen(
                    onRegistrationSuccess = {  },
                    onNavigateToLogin = {
                        navController.navigate(NavRoutes.LOGIN) {
                            popUpTo(NavRoutes.REGISTER) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(NavRoutes.HOME) {
                HomeScreen(
                    navController = navController,
                    onSignOut = {
                        firebaseAuth.signOut()
                    }
                )
            }
            composable(NavRoutes.TEST_SCREEN) {
                TestScreen(navController = navController)
            }
            composable(NavRoutes.MINI_GAME_SCREEN) {
                MiniGameScreen(navController = navController)
            }
        }
    }
}
