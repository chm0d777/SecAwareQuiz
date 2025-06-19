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
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.data.ThemeRepository
import com.chmod777.secawarequiz.navigation.AppNavigation
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.components.AppBottomNavigationBar
import com.chmod777.secawarequiz.ui.theme.PrimaryText
import com.chmod777.secawarequiz.ui.theme.SecAwareQuizTheme
import com.chmod777.secawarequiz.ui.theme.ThemePreferences
import com.chmod777.secawarequiz.utils.getCurrentAppLanguage
import com.chmod777.secawarequiz.utils.setAppLanguage
import com.chmod777.secawarequiz.viewmodels.quiz.QuizViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log

class MainActivity : ComponentActivity() {

    private lateinit var themeRepository: ThemeRepository

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        themeRepository = ThemeRepository(this)
        val lang = themeRepository.getLanguagePreference()
        setAppLanguage(this, lang)

        super.onCreate(savedInstanceState)
        applyThemePreference()

        setContent {

            lifecycleScope.launch {

                val savedStateHandle = SavedStateHandle()
                val quizViewModel = QuizViewModel(savedStateHandle)

                Log.d("QuizSim", "Initial State: ${quizViewModel.uiState.value}")


                var loaded = false
                for (i in 1..10) {
                    if (!quizViewModel.uiState.value.isLoading && quizViewModel.uiState.value.currentQuestion != null) {
                        Log.d("QuizSim", "Quiz loaded. Current question: ${quizViewModel.uiState.value.currentQuestion?.text}")
                        loaded = true
                        break
                    }
                    delay(100)
                }

                if (!loaded) {
                    Log.d("QuizSim", "Quiz did not load in time for simulation.")
                }

                quizViewModel.uiState.value.currentQuestion?.let { question ->
                    if (question.options.isNotEmpty()) {
                        val firstOptionId = question.options[0].id
                        Log.d("QuizSim", "Selecting first option: ID = $firstOptionId, Text = ${question.options[0].text}")
                        quizViewModel.onOptionSelected(firstOptionId)
                        Log.d("QuizSim", "State after selecting option: ${quizViewModel.uiState.value}")

                        Log.d("QuizSim", "Submitting answer...")
                        quizViewModel.onSubmitAnswer()
                        Log.d("QuizSim", "State after submitting answer: ${quizViewModel.uiState.value}")

                        Log.d("QuizSim", "Going to next question...")
                        quizViewModel.onNextQuestion()
                        Log.d("QuizSim", "State after next question: ${quizViewModel.uiState.value}")
                    } else {
                        Log.d("QuizSim", "Current question has no options.")
                    }
                } ?: run {
                    if (loaded) Log.d("QuizSim", "No current question available even after loading.")
                }
            }


            val initialDarkTheme = when (themeRepository.getThemePreference()) {
                ThemePreferences.THEME_DARK -> true
                ThemePreferences.THEME_LIGHT -> false
                else -> resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
            var isDarkTheme by remember { mutableStateOf(initialDarkTheme) }
            var showSettingsMenu by remember { mutableStateOf(false) }
            var currentLanguage by remember { mutableStateOf(themeRepository.getLanguagePreference()) }
            val context = LocalContext.current

            LaunchedEffect(isDarkTheme) {
                applyThemePreference(isDarkTheme)
            }


            SecAwareQuizTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val firebaseAuth = FirebaseAuth.getInstance()
                val currentUser = firebaseAuth.currentUser
                val startDestination = if (currentUser != null) Screen.Home.route else Screen.Login.route

                val bottomNavItems = listOf(Screen.Home, Screen.AllTests, Screen.Profile)

                Scaffold(
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentRoute == Screen.Home.route) {
                            TopAppBar(
                                title = { Text(stringResource(id = R.string.app_title_full), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                                actions = {
                                    Box {
                                        IconButton(onClick = { showSettingsMenu = true }) {
                                            Icon(
                                                imageVector = Icons.Filled.Settings,
                                                contentDescription = stringResource(id = R.string.home_settings_icon_desc)
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
                                                    val newThemePreference = if (isDarkTheme) ThemePreferences.THEME_LIGHT else ThemePreferences.THEME_DARK
                                                    val newIsDarkState = when (newThemePreference) {
                                                        ThemePreferences.THEME_LIGHT -> false
                                                        ThemePreferences.THEME_DARK -> true
                                                        else -> false
                                                    }
                                                    themeRepository.saveThemePreference(newThemePreference)
                                                    isDarkTheme = newIsDarkState
                                                    showSettingsMenu = false
                                                }) {
                                                    Icon(
                                                        imageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                                                        contentDescription = stringResource(id = R.string.settings_dropdown_theme_toggle_desc),
                                                        tint = MaterialTheme.colorScheme.onSurface
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
                                                    val newLang = if (currentLanguage == ThemePreferences.LANGUAGE_RUSSIAN) ThemePreferences.LANGUAGE_ENGLISH else ThemePreferences.LANGUAGE_RUSSIAN
                                                    themeRepository.saveLanguagePreference(newLang)
                                                    setAppLanguage(context, newLang)
                                                    currentLanguage = newLang
                                                    showSettingsMenu = false
                                                    (context as? android.app.Activity)?.recreate()
                                                }) {
                                                    Text(
                                                        text = if (currentLanguage == ThemePreferences.LANGUAGE_RUSSIAN) "EN" else "RU",
                                                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface,
                                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        } else if (currentRoute == Screen.Profile.route) {
                            TopAppBar(
                                title = { Text(stringResource(id = R.string.profile_screen_title), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)) },
                                navigationIcon = {
                                     val previousRoute = navController.previousBackStackEntry?.destination?.route
                                     val isPreviousBottomNavItem = bottomNavItems.any { it.route == previousRoute }
                                     if (navController.previousBackStackEntry != null && !isPreviousBottomNavItem) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.common_back))
                                        }
                                     }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
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
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
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

    private fun applyThemePreference(isDarkCurrently: Boolean? = null) {
        val preference = themeRepository.getThemePreference()
        val nightMode = when (preference) {
            ThemePreferences.THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemePreferences.THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }
}
