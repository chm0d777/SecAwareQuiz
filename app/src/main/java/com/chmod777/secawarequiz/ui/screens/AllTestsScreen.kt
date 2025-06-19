package com.chmod777.secawarequiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.SecurityUpdateGood
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.data.model.TestListItem // Import moved model
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.components.TestSelectionCard
import com.chmod777.secawarequiz.ui.theme.*
import com.chmod777.secawarequiz.viewmodels.AllTestsViewModel


// data class TestListItem(val id: String, val title: String, val description: String, val route: String) // Removed local definition

@Composable
fun AllTestsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    allTestsViewModel: AllTestsViewModel = viewModel() // Inject ViewModel
) {
    val allTestsAndGames by allTestsViewModel.testItems.collectAsState() // Collect from ViewModel

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // THEME Color
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(allTestsAndGames) { testItem ->
                val (icon, color) = when (testItem.id) {
                    "quiz1" -> Icons.Filled.SecurityUpdateGood to CardAccentLiteracy
                    "minigame1" -> Icons.Filled.Extension to CardAccentPhishing
                    "fakelogin" -> Icons.Filled.Quiz to CardAccentDataSecurity
                    else -> Icons.Filled.Quiz to MaterialTheme.colorScheme.primary
                }
                TestSelectionCard(
                    title = stringResource(id = testItem.titleResId), // Use stringResource
                    icon = icon,
                    accentColor = color,
                    contentDescription = "Icon for " + stringResource(id = testItem.titleResId), // Use stringResource
                    description = stringResource(id = testItem.descriptionResId), // Use stringResource
                    onClick = { navController.navigate(testItem.route) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllTestsScreenPreview() {
    SecAwareQuizTheme {
        // Preview will likely not work correctly with default ViewModel instantiation
        // without Hilt or a custom factory. For now, let it be.
        // It might show an empty screen or crash if ViewModel relies on complex dependencies not available in preview.
        AllTestsScreen(navController = rememberNavController(), modifier = Modifier)
    }
}