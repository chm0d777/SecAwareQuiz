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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.theme.*
import com.chmod777.secawarequiz.viewmodels.profile.ProfileViewModel

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
                tint = ProfileItemIconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = PrimaryText),
                modifier = Modifier.weight(1f)
            )
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(color = HomeSubtitleText),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.profile_item_navigation_arrow_desc),
                tint = ProfileItemIconColor
            )
        }
        Divider(color = BottomNavBorder, thickness = 0.5.dp)
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = stringResource(id = R.string.profile_avatar_desc),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = firebaseUser?.displayName ?: stringResource(R.string.profile_name_not_set),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = PrimaryText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = firebaseUser?.email ?: stringResource(R.string.profile_email_not_set),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, color = HomeSubtitleText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = stringResource(id = R.string.profile_section_account).uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        )
        ProfileItemRow(icon = Icons.Filled.PersonOutline, iconContentDescription = stringResource(id = R.string.profile_icon_edit_profile_desc), title = stringResource(id = R.string.profile_item_edit_profile), onClick = { })
        ProfileItemRow(icon = Icons.Filled.LockOpen, iconContentDescription = stringResource(id = R.string.profile_icon_change_password_desc), title = stringResource(id = R.string.profile_item_change_password), onClick = { })
        ProfileItemRow(icon = Icons.Filled.NotificationsNone, iconContentDescription = stringResource(id = R.string.profile_icon_notifications_desc), title = stringResource(id = R.string.profile_item_notifications), onClick = { })

        Text(
            text = stringResource(id = R.string.profile_section_settings).uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
        )
        ProfileItemRow(icon = Icons.Filled.WbSunny, iconContentDescription = stringResource(id = R.string.profile_icon_theme_desc), title = stringResource(id = R.string.profile_item_theme), value = uiState.currentThemeValue, onClick = { })
        ProfileItemRow(icon = Icons.Filled.Language, iconContentDescription = stringResource(id = R.string.profile_icon_language_desc), title = stringResource(id = R.string.profile_item_language), value = uiState.currentLanguageValue, onClick = { })
        ProfileItemRow(icon = Icons.Filled.Info, iconContentDescription = stringResource(id = R.string.profile_icon_about_desc), title = stringResource(id = R.string.profile_item_about), onClick = { })

        Spacer(modifier = Modifier.weight(1f))

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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    SecAwareQuizTheme {
        ProfileScreen(navController = rememberNavController(), modifier = Modifier.padding(0.dp))
    }
}
