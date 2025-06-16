package com.chmod777.secawarequiz.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme


data class TestListItem(val id: String, val title: String, val description: String, val route: String)

@Composable
fun AllTestsScreen(navController: NavController, modifier: Modifier = Modifier) {
    val allTestsAndGames = listOf(
        TestListItem("quiz1", "URL Safety Quiz", "Test your knowledge about safe URLs.", Screen.Quiz.createRoute(1)),
        TestListItem("minigame1", "Phishing Master", "Identify phishing attempts.", Screen.Minigame1.route),
        TestListItem("fakelogin", "Fake Login Spotter", "Can you spot the fake login page?", Screen.FakeLoginGame.route),
        TestListItem("quiz2", "Password Strength Test", "Learn about strong passwords.", Screen.Quiz.createRoute(2)),
        TestListItem("quiz3", "Social Engineering Awareness", "Understand social engineering tactics.", Screen.Quiz.createRoute(3)),
        TestListItem("minigame2", "Data Breach Game", "Simulate responding to a data breach.", Screen.Home.route)
    )

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(allTestsAndGames) { testItem ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(testItem.route) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(testItem.title, style = MaterialTheme.typography.titleMedium)
                    Text(testItem.description, style = MaterialTheme.typography.bodySmall)
                }
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllTestsScreenPreview() {
    SecAwareQuizTheme {
        AllTestsScreen(navController = rememberNavController(), modifier = Modifier.padding(0.dp))
    }
}
