package com.chmod777.secawarequiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.navigation.AppNavigation
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.components.AppBottomNavigationBar
import com.chmod777.secawarequiz.ui.theme.PrimaryText
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme
import com.chmod777.secawarequiz.utils.getCurrentAppLanguage
import com.chmod777.secawarequiz.utils.setAppLanguage
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var showSettingsMenu by remember { mutableStateOf(false) }
            var isDarkTheme by remember { mutableStateOf(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) }
            var currentLanguage by remember { mutableStateOf(getCurrentAppLanguage()) }
            val context = LocalContext.current

            SecAwareQuizTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val firebaseAuth = FirebaseAuth.getInstance()
                val currentUser = firebaseAuth.currentUser
                val startDestination = if (currentUser != null) Screen.Home.route else Screen.Login.route

                val bottomNavItems = listOf(Screen.Home, Screen.Profile, Screen.AllTests)

                Scaffold(
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentRoute == Screen.Home.route) {
                            TopAppBar(
                                title = { Text("SecAware Quiz", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = PrimaryText)) },
                                actions = {
                                    Box {
                                        IconButton(onClick = { showSettingsMenu = true }) {
                                            Icon(
                                                imageVector = Icons.Filled.Settings,
                                                contentDescription = stringResource(id = R.string.home_settings_icon_desc),
                                                tint = PrimaryText
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = showSettingsMenu,
                                            onDismissRequest = { showSettingsMenu = false }
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                IconButton(onClick = {
                                                    isDarkTheme = !isDarkTheme
                                                    AppCompatDelegate.setDefaultNightMode(if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                                                    showSettingsMenu = false
                                                }) {
                                                    Icon(
                                                        imageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                                                        contentDescription = stringResource(id = R.string.settings_dropdown_theme_toggle_desc),
                                                        tint = PrimaryText
                                                    )
                                                }
                                            }
                                            Divider()
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                IconButton(onClick = {
                                                    val newLang = if (currentLanguage == "ru") "en" else "ru"
                                                    setAppLanguage(context, newLang)
                                                    currentLanguage = newLang
                                                    showSettingsMenu = false
                                                    (context as? android.app.Activity)?.recreate()
                                                }) {
                                                    Text(
                                                        text = if (currentLanguage == "ru") "EN" else "RU",
                                                        fontWeight = FontWeight.Bold, color = PrimaryText,
                                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.White,
                                    titleContentColor = PrimaryText,
                                    actionIconContentColor = PrimaryText
                                )
                            )
                        } else if (currentRoute == Screen.Profile.route) {
                            TopAppBar(
                                title = { Text(stringResource(id = R.string.profile_screen_title), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold, color = PrimaryText)) },
                                navigationIcon = {
                                     val previousRoute = navController.previousBackStackEntry?.destination?.route
                                     val isPreviousBottomNavItem = bottomNavItems.any { it.route == previousRoute }
                                     if (navController.previousBackStackEntry != null && !isPreviousBottomNavItem) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.common_back), tint = PrimaryText)
                                        }
                                     }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.White,
                                    titleContentColor = PrimaryText,
                                    navigationIconContentColor = PrimaryText
                                )
                            )
                        } else if (currentRoute == Screen.AllTests.route){
                             TopAppBar(
                                title = { Text(stringResource(id = R.string.all_tests_screen_title)) },
                                navigationIcon = {
                                     if (navController.previousBackStackEntry != null) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.common_back))
                                        }
                                     }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.White,
                                    titleContentColor = PrimaryText,
                                    navigationIconContentColor = PrimaryText
                                )
                            )
                        }
                    },
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        val showBottomBar = bottomNavItems.any { it.route == currentRoute }

                        if (showBottomBar) {
                            AppBottomNavigationBar(navController = navController, items = bottomNavItems)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
