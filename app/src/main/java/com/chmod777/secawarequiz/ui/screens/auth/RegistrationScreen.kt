package com.chmod777.secawarequiz.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chmod777.secawarequiz.R
import com.chmod777.secawarequiz.navigation.NavRoutes
import com.chmod777.secawarequiz.ui.theme.*
import com.chmod777.secawarequiz.viewmodels.auth.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavHostController,
    viewModel: RegistrationViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    if (uiState.error != null) {
        Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
        viewModel.clearError()
    }

    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            navController.navigate(NavRoutes.LOGIN_SCREEN) {
                popUpTo(NavRoutes.REGISTRATION_SCREEN) { inclusive = true }
            }
            viewModel.onRegistrationSuccessNavigationConsumed()
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
            text = stringResource(id = R.string.login_title),
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
            text = stringResource(id = R.string.registration_heading),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
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
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            textStyle = TextStyle(color = PrimaryText, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(stringResource(id = R.string.password_hint_registration), color = HintGray) },
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
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = HintGray
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            textStyle = TextStyle(color = PrimaryText, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text(stringResource(id = R.string.confirm_password_hint), color = HintGray) },
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
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        tint = HintGray
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    viewModel.onRegisterClicked(context, email, password, confirmPassword)
                }
            ),
            singleLine = true,
            textStyle = TextStyle(color = PrimaryText, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.onRegisterClicked(context, email, password, confirmPassword)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                contentColor = Color.White
            ),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(stringResource(id = R.string.register_button_text), style = TextStyle(fontSize = 16.sp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val promptText = stringResource(id = R.string.registration_to_login_prompt)
        val actionText = stringResource(id = R.string.registration_to_login_action)
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PrimaryText, fontSize = 14.sp)) {
                append(promptText)
                append(" ")
            }
            pushStringAnnotation(tag = "LOGIN_ACTION", annotation = "login")
            withStyle(style = SpanStyle(color = AccentBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)) {
                append(actionText)
            }
            pop()
        }

        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "LOGIN_ACTION", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate(NavRoutes.LOGIN_SCREEN) {
                             popUpTo(NavRoutes.REGISTRATION_SCREEN) { inclusive = true }
                        }
                    }
            },
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, fontSize = 14.sp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RegistrationScreenPreview() {
    SecAwareQuizTheme {
        RegistrationScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF000000)
@Composable
fun RegistrationScreenDarkPreview() {
    SecAwareQuizTheme(darkTheme = true) {
         Surface(color = Color.Black) {
            RegistrationScreen(navController = rememberNavController())
        }
    }
}
