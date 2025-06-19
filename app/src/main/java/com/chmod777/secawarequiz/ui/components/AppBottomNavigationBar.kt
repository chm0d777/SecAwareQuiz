package com.chmod777.secawarequiz.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme // Added import
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation // Added import
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Added import
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.Screen
// import com.chmod777.secawarequiz.ui.theme.BottomNavActive // To be replaced
// import com.chmod777.secawarequiz.ui.theme.BottomNavBorder // To be removed
// import com.chmod777.secawarequiz.ui.theme.BottomNavInactive // To be replaced
import com.chmod777.secawarequiz.ui.theme.LightBlueNavIndicator // Added import
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme

@Composable
fun AppBottomNavigationBar(navController: NavController, items: List<Screen>) {
    NavigationBar(
        // modifier = Modifier.border(width = 0.5.dp, color = BottomNavBorder), // Removed border
        containerColor = MaterialTheme.colorScheme.background, // Changed to background for white in light theme
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val title = when(screen) {
                Screen.Home -> stringResource(id = R.string.bottom_nav_home)
                Screen.AllTests -> stringResource(id = R.string.bottom_nav_tests)
                Screen.Profile -> stringResource(id = R.string.bottom_nav_profile)
                else -> ""
            }
            val icon = when(screen) {
                Screen.Home -> Icons.Filled.Home
                Screen.AllTests -> Icons.AutoMirrored.Filled.ViewList
                Screen.Profile -> Icons.Filled.Person
                else -> Icons.Filled.Home
            }
            val contentDesc = when(screen) {
                Screen.Home -> stringResource(id = R.string.bottom_nav_home_icon_desc)
                Screen.AllTests -> stringResource(id = R.string.bottom_nav_tests_icon_desc)
                Screen.Profile -> stringResource(id = R.string.bottom_nav_profile_icon_desc)
                else -> ""
            }

            NavigationBarItem(
                icon = { Icon(icon, contentDescription = contentDesc) },
                label = { Text(title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, // Changed for contrast with new indicator
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = Color.White, // Changed for contrast with new indicator
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = LightBlueNavIndicator // User requested color #0D80F2
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewAppBottomNavigationBar() {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.AllTests, Screen.Profile) // Added AllTests for preview
    SecAwareQuizTheme {
        AppBottomNavigationBar(navController = navController, items = items)
    }
}
