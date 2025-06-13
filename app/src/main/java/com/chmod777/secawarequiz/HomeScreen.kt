package com.chmod777.secawarequiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController, onSignOut: () -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentUser != null) {
            Text("Welcome!", style = MaterialTheme.typography.headlineSmall)
            Text("UID: ${currentUser.uid}", style = MaterialTheme.typography.bodySmall)
            Text("Email: ${currentUser.email ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Text("Welcome to Home Screen!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = { navController.navigate(NavRoutes.TEST_SCREEN) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Test")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(NavRoutes.MINI_GAME_SCREEN) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Play Mini-Game")
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Out")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(navController = rememberNavController(), onSignOut = {})
    }
}
