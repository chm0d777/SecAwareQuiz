package com.chmod777.secawarequiz.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.ui.theme.*
import com.chmod777.secawarequiz.viewmodels.auth.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.passwordResetSuccessMessage) {
        if (uiState.passwordResetSuccessMessage != null) {
            Toast.makeText(context, uiState.passwordResetSuccessMessage, Toast.LENGTH_LONG).show()
            loginViewModel.clearPasswordResetMessages()
        }
    }
    LaunchedEffect(key1 = uiState.passwordResetError) {
        if (uiState.passwordResetError != null) {
            Toast.makeText(context, uiState.passwordResetError, Toast.LENGTH_LONG).show()
            loginViewModel.clearPasswordResetMessages()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.password_reset_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                fontSize = 32.sp,
                fontFamily = FontFamily.SansSerif
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.password_reset_instruction),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = PrimaryText,
                fontSize = 16.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(stringResource(id = R.string.email_hint), color = HintGray) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = BorderColor,
                cursorColor = AccentBlue,
                focusedLabelColor = AccentBlue,
                unfocusedLabelColor = HintGray,
                errorCursorColor = md_theme_light_error,
                errorBorderColor = md_theme_light_error,
                errorLabelColor = md_theme_light_error,
                focusedTextColor = PrimaryText,
                unfocusedTextColor = PrimaryText
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    loginViewModel.sendPasswordResetEmail(context, email)
                }
            ),
            singleLine = true,
            textStyle = TextStyle(color = PrimaryText, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                loginViewModel.sendPasswordResetEmail(context, email)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                contentColor = Color.White
            ),
            enabled = !uiState.isPasswordResetLoading
        ) {
            if (uiState.isPasswordResetLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    stringResource(id = R.string.password_reset_button_text),
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text(
                text = stringResource(id = R.string.password_reset_back_to_login),
                color = AccentBlue,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PasswordResetScreenPreview() {
    SecAwareQuizTheme {
        PasswordResetScreen(navController = rememberNavController(), loginViewModel = viewModel())
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF000000)
@Composable
fun PasswordResetScreenDarkPreview() {
    SecAwareQuizTheme(darkTheme = true) {
        Surface(color = Color.Black) {
            PasswordResetScreen(navController = rememberNavController(), loginViewModel = viewModel())
        }
    }
}
