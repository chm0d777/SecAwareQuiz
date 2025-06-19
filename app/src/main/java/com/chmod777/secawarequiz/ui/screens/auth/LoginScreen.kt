package com.chmod777.secawarequiz.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import android.content.ActivityNotFoundException
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.chmod777.secawarequiz.navigation.Screen
import com.chmod777.secawarequiz.ui.theme.*
import com.chmod777.secawarequiz.viewmodels.auth.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken
                if (idToken != null) {
                    viewModel.firebaseAuthWithGoogle(context, idToken)
                } else {
                    viewModel.setGoogleSignInError("Failed to get ID token from Google.")
                }
            } catch (e: ApiException) {
                viewModel.setGoogleSignInError("Google Sign In failed: ${e.localizedMessage}")
            }
        } else {
            val errorMsg = "Google Sign In attempt was cancelled or failed. Result code: ${result.resultCode}"
            Log.w("LoginScreenGoogleSignIn", errorMsg)
            viewModel.setGoogleSignInError(errorMsg)
        }
    }

    if (uiState.error != null) {
        Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
        viewModel.clearError()
    }

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            navController.navigate(NavRoutes.HOME_SCREEN) {
                popUpTo(NavRoutes.LOGIN_SCREEN) { inclusive = true }
            }
            viewModel.onLoginSuccessNavigationConsumed()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
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

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.login_subtitle),
            style = MaterialTheme.typography.titleLarge.copy(
                color = PrimaryText,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.username_or_email_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputFieldBackground,
                unfocusedContainerColor = InputFieldBackground,
                disabledContainerColor = InputFieldBackground,
                focusedBorderColor = LinkColor,
                unfocusedBorderColor = BorderColor,
                cursorColor = LinkColor,
                focusedLabelColor = LinkColor,
                unfocusedLabelColor = BorderColor
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputFieldBackground,
                unfocusedContainerColor = InputFieldBackground,
                disabledContainerColor = InputFieldBackground,
                focusedBorderColor = LinkColor,
                unfocusedBorderColor = BorderColor,
                cursorColor = LinkColor,
                focusedLabelColor = LinkColor,
                unfocusedLabelColor = BorderColor
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    viewModel.onLoginClicked(context, email, password)
                }
            ),
            singleLine = true,
            textStyle = TextStyle(color = PrimaryText, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate(Screen.PasswordReset.route) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(id = R.string.forgot_password_link),
                color = LinkColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.onLoginClicked(context, email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LinkColor,
                contentColor = ButtonTextColor
            ),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = ButtonTextColor
                )
            } else {
                Text(stringResource(id = R.string.login_button_text), style = TextStyle(fontSize = 16.sp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                viewModel.clearError()
                val signInIntent = viewModel.getGoogleSignInIntent(context)
                if (signInIntent != null) {
                    try {
                        googleSignInLauncher.launch(signInIntent)
                    } catch (e: ActivityNotFoundException) {
                        Log.e("LoginScreenGoogleSignIn", "Google Sign In Intent could not be launched: ${e.localizedMessage}")
                        viewModel.setGoogleSignInError("Google Sign In is not available on this device.")
                    }
                } else {
                    viewModel.setGoogleSignInError("Could not get Google Sign In Intent. Check configuration.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, BorderColor),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = GoogleButtonTextColor
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google logo",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(id = R.string.google_login_button_text),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = GoogleButtonTextColor,
                        fontSize = 16.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val promptText = stringResource(id = R.string.login_to_register_prompt)
        val actionText = stringResource(id = R.string.login_to_register_action)
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PrimaryText, fontSize = 14.sp)) {
                append(promptText)
                append(" ")
            }
            pushStringAnnotation(tag = "REGISTER_ACTION", annotation = "register")
            withStyle(style = SpanStyle(color = LinkColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)) {
                append(actionText)
            }
            pop()
        }

        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "REGISTER_ACTION", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate(NavRoutes.REGISTRATION_SCREEN)
                    }
            },
            modifier = Modifier.padding(top = 16.dp),
            style = TextStyle(textAlign = TextAlign.Center)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
@Composable
fun LoginScreenPreview() {
    SecAwareQuizTheme {
        LoginScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenDarkPreview() {
    SecAwareQuizTheme(darkTheme = true) {
        LoginScreen(navController = rememberNavController())
    }
}
