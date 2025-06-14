package com.chmod777.secawarequiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.navigation.AppNavigation
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            SecAwareQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentUser = firebaseAuth.currentUser
                    val startDestination = if (currentUser != null) Screen.Home.route else Screen.Login.route

                    AppNavigation(navController = rememberNavController(), startDestination = startDestination)
                }
            }
        }
    }
}
