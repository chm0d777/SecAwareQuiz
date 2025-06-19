package com.chmod777.secawarequiz.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.theme.*
import com.chmod777.secawarequiz.viewmodels.profile.ProfileViewModel
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun ProfileItemRow(
    icon: ImageVector,
    iconContentDescription: String,
    title: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.profile_item_navigation_arrow_desc),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
    }
}

@Composable
fun ProfileScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle(lifecycleOwner = LocalLifecycleOwner.current)
    val firebaseUser = uiState.firebaseUser

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val photoUrl = firebaseUser?.photoUrl?.toString()
                val painter = rememberAsyncImagePainter(
                    model = photoUrl,
                    placeholder = painterResource(id = R.drawable.ic_google_logo),
                    error = painterResource(id = R.drawable.ic_google_logo)
                )

                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.profile_avatar_desc),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = firebaseUser?.displayName ?: stringResource(R.string.profile_name_not_set),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = firebaseUser?.email ?: stringResource(R.string.profile_email_not_set),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = stringResource(id = R.string.profile_section_account).uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            ProfileItemRow(icon = Icons.Filled.PersonOutline, iconContentDescription = stringResource(id = R.string.profile_icon_edit_profile_desc), title = stringResource(id = R.string.profile_item_edit_profile), onClick = { navController.navigate(Screen.EditProfile.route) })
            ProfileItemRow(icon = Icons.Filled.LockOpen, iconContentDescription = stringResource(id = R.string.profile_icon_change_password_desc), title = stringResource(id = R.string.profile_item_change_password), onClick = { navController.navigate(Screen.ChangePassword.route) })
            ProfileItemRow(icon = Icons.Filled.NotificationsNone, iconContentDescription = stringResource(id = R.string.profile_icon_notifications_desc), title = stringResource(id = R.string.profile_item_notifications), onClick = { navController.navigate(Screen.NotificationsSettings.route) })

            Text(
                text = stringResource(id = R.string.profile_section_settings).uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
            )
            ProfileItemRow(
                icon = Icons.Filled.WbSunny,
                iconContentDescription = stringResource(id = R.string.profile_icon_theme_desc),
                title = stringResource(id = R.string.profile_item_theme),
                value = when (uiState.currentThemePreference) {
                    ThemePreferences.THEME_LIGHT -> stringResource(R.string.theme_light)
                    ThemePreferences.THEME_DARK -> stringResource(R.string.theme_dark)
                    ThemePreferences.THEME_SYSTEM -> stringResource(R.string.theme_system)
                    else -> stringResource(R.string.theme_system)
                },
                onClick = { profileViewModel.onThemeSettingsClicked() }
            )
            ProfileItemRow(
                icon = Icons.Filled.Language,
                iconContentDescription = stringResource(id = R.string.profile_icon_language_desc),
                title = stringResource(id = R.string.profile_item_language),
                value = when (uiState.currentLanguagePreference) {
                    ThemePreferences.LANGUAGE_ENGLISH -> stringResource(R.string.language_english)
                    ThemePreferences.LANGUAGE_RUSSIAN -> stringResource(R.string.language_russian)
                    else -> stringResource(R.string.language_english)
                },
                onClick = { profileViewModel.onLanguageSettingsClicked() }
            )
            ProfileItemRow(icon = Icons.Filled.Info, iconContentDescription = stringResource(id = R.string.profile_icon_about_desc), title = stringResource(id = R.string.profile_item_about), onClick = { navController.navigate(Screen.AboutApp.route) })

            Spacer(modifier = Modifier.weight(1f, fill = true))

            if (uiState.showThemeDialog) {
                ThemeSelectionDialog(
                    currentTheme = uiState.currentThemePreference,
                    onThemeSelected = { theme ->
                        profileViewModel.onThemeSelected(theme)
                        val activity = navController.context as? android.app.Activity
                        activity?.recreate()
                    },
                    onDismiss = { profileViewModel.onThemeDialogDismiss() }
                )
            }

            if (uiState.showLanguageDialog) {
                LanguageSelectionDialog(
                    currentLanguage = uiState.currentLanguagePreference,
                    onLanguageSelected = { languageCode ->
                        profileViewModel.onLanguageSelected(languageCode)
                        val activity = navController.context as? android.app.Activity
                        activity?.recreate()
                    },
                    onDismiss = { profileViewModel.onLanguageDialogDismiss() }
                )
            }

            TextButton(
                onClick = {
                    profileViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.padding(end = 8.dp))
                Text(stringResource(R.string.common_sign_out), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val themeOptions = listOf(
        ThemePreferences.THEME_LIGHT to stringResource(R.string.theme_light),
        ThemePreferences.THEME_DARK to stringResource(R.string.theme_dark),
        ThemePreferences.THEME_SYSTEM to stringResource(R.string.theme_system)
    )
    var selectedOption by remember { mutableStateOf(themeOptions.firstOrNull { it.first == currentTheme } ?: themeOptions.last()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.theme_selection_dialog_title)) },
        text = {
            Column(Modifier.selectableGroup()) {
                themeOptions.forEach { (themePreferenceName, themeDisplayName) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (themePreferenceName == selectedOption.first),
                                onClick = { selectedOption = themePreferenceName to themeDisplayName }
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (themePreferenceName == selectedOption.first),
                            onClick = null
                        )
                        Text(
                            text = themeDisplayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onThemeSelected(selectedOption.first)
                    onDismiss()
                }
            ) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languageOptions = listOf(
        ThemePreferences.LANGUAGE_ENGLISH to stringResource(R.string.language_english),
        ThemePreferences.LANGUAGE_RUSSIAN to stringResource(R.string.language_russian)
    )
    var selectedOption by remember { mutableStateOf(languageOptions.firstOrNull { it.first == currentLanguage } ?: languageOptions.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.language_selection_dialog_title)) },
        text = {
            Column(Modifier.selectableGroup()) {
                languageOptions.forEach { (langCode, langDisplayName) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (langCode == selectedOption.first),
                                onClick = { selectedOption = langCode to langDisplayName }
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (langCode == selectedOption.first),
                            onClick = null
                        )
                        Text(
                            text = langDisplayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onLanguageSelected(selectedOption.first)
                    onDismiss()
                }
            ) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

