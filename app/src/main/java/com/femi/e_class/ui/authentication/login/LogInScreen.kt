package com.femi.e_class.ui.authentication.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.femi.e_class.R
import com.femi.e_class.data.handleNetworkExceptions
import com.femi.e_class.navigation.Screen
import com.femi.e_class.ui.user.UserActivity
import com.femi.e_class.viewmodels.AuthenticationViewModel
import com.femi.e_class.presentation.authentication.logIn.LogInFormEvent

@Composable
fun LogInScreen(
    navController: NavHostController,
    viewModel: AuthenticationViewModel,
    email: String?,
    password: String?,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val state by viewModel.logInFormState.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(20.dp, 0.dp, 30.dp, 20.dp),
    ) {
        LaunchedEffect(key1 = true) {
            if (email != null && password != null) {
                viewModel.onEvent(LogInFormEvent.EmailChanged(email))
                viewModel.onEvent(LogInFormEvent.PasswordChanged(password))
                viewModel.onEvent(LogInFormEvent.Submit)
            }
        }
        LaunchedEffect(key1 = context) {
            viewModel.logInFormEvents.collect { event ->
                when (event) {
                    is AuthenticationViewModel.FormEvent.Success -> {
                        viewModel.logUserIn(
                            email = state.email,
                            password = state.password
                        )
                    }
                }
            }
        }
        LaunchedEffect(key1 = state) {
            viewModel.logInEvents.collect { event ->
                loading = (event is AuthenticationViewModel.LogInEvent.Loading)
                when (event) {
                    is AuthenticationViewModel.LogInEvent.Success -> {
                        moveToUserActivity(context)
                    }
                    is AuthenticationViewModel.LogInEvent.Error -> {
                        context.handleNetworkExceptions(
                            exception = event.exception
                        )
                    }
                    is AuthenticationViewModel.LogInEvent.Loading -> {}
                    is AuthenticationViewModel.LogInEvent.NoUser -> {
                        context.handleNetworkExceptions(
                            message = event.message
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            value = state.email,
            label = { Text(text = "Email") },
            onValueChange = {
                viewModel.onEvent(LogInFormEvent.EmailChanged(it))
            },
            isError = state.emailError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.Email, "Email Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )

        )
        if (state.emailError != null) {
            Text(
                text = state.emailError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.password,
            label = { Text(text = "Password") },
            onValueChange = {
                viewModel.onEvent(LogInFormEvent.PasswordChanged(it))
            },
            isError = state.passwordError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_visibility),
                            contentDescription = "Show Password"
                        )
                    }
                } else {
                    IconButton(onClick = { showPassword = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_visibility_off),
                            contentDescription = "Hide Password"
                        )
                    }
                }
            },
            leadingIcon = {
                Icon(Icons.Filled.Lock, "Password Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Done
            )
        )
        if (state.passwordError != null) {
            Text(
                text = state.passwordError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        TextButton(
            onClick = {
                navController.navigate(Screen.ResetPasswordScreen.route)
            },
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(text = "Forgot Password?")
        }
        if (loading) {
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(20.dp))
        }
        Button(
            onClick = {
                viewModel.onEvent(LogInFormEvent.Submit)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            enabled = !loading
        ) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(40.dp))
        val annotatedText = buildAnnotatedString {
            pushStringAnnotation(tag = "Create Account Button",
                annotation = "Create Account")

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                ),
            ) {
                append("Create Account? ")
            }

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                ),
            ) {
                append("Create Account")
            }

            pop()
        }
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "Create Account Button",
                    start = offset,
                    end = offset)
                    .firstOrNull()?.let {
                        navController.navigate(Screen.SignUpScreen.route) {
                            popUpTo(Screen.OnBoardingScreen.route)
                            launchSingleTop = true
                        }
                    }
            }

        )
    }
}


private fun moveToUserActivity(context: Context) {
    val activity = context as Activity

    Intent(activity, UserActivity::class.java).also { intent ->
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
        activity.finish()
    }
}