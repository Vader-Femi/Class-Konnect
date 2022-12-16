package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.femi.e_class.*
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityLoginBinding
import com.femi.e_class.presentation.LogInFormEvent
import com.femi.e_class.repositories.LogInRepository
import com.femi.e_class.viewmodels.LogInViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LogInViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val createdEmail = intent.getStringExtra(KEY_EMAIL)
        val createdPassword = intent.getStringExtra(KEY_PASSWORD)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val scrollState = rememberScrollState()
                        val context = LocalContext.current
                        val state = viewModel.logInFormState
                        var showPassword by remember { mutableStateOf(false) }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(30.dp, 60.dp, 30.dp, 30.dp),
                        ) {
                            var loading by remember { mutableStateOf(false) }
                            LaunchedEffect(key1 = context) {
                                viewModel.validationEvents.collect { event ->
                                    when (event) {
                                        is LogInViewModel.ValidationEvent.Success -> {
                                            viewModel.logInUser(
                                                email = viewModel.logInFormState.email,
                                                password = viewModel.logInFormState.password
                                            )
                                        }
                                    }
                                }
                            }
                            LaunchedEffect(key1 = state) {
                                viewModel.logInEvents.collect { event ->
                                    loading = (event is LogInViewModel.LogInEvent.Loading)
                                    when (event) {
                                        is LogInViewModel.LogInEvent.Success -> {
                                            moveToDashboard()
                                        }
                                        is LogInViewModel.LogInEvent.Error -> {
                                            handleNetworkExceptions(event.exception,
                                                retry = { viewModel.onEvent(LogInFormEvent.Submit) })
                                        }
                                        is LogInViewModel.LogInEvent.Loading -> {}
                                        is LogInViewModel.LogInEvent.NoUser -> {
                                            handleNetworkExceptions(message = event.message,
                                                retry = { viewModel.onEvent(LogInFormEvent.Submit) })
                                        }
                                    }
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start),
                                text = "Sign In",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start),
                                text = "Enter your details to get started",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            OutlinedTextField(
                                value = createdEmail ?: state.email,
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
                                    text = state.emailError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            OutlinedTextField(
                                value = createdPassword ?: state.password,
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
                                    text = state.passwordError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            if (loading) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                            Button(
                                onClick = {
                                    if (!createdEmail.isNullOrEmpty() &&
                                        !createdPassword.isNullOrEmpty()
                                    ) {
                                        viewModel.onEvent(LogInFormEvent.EmailChanged(createdEmail))
                                        viewModel.onEvent(LogInFormEvent.PasswordChanged(createdPassword))
                                    }
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
                                            startActivity(Intent(this@LoginActivity,
                                                SignUpActivity::class.java))
                                            finish()
                                        }
                                }

                            )
                        }
                    }
                }
            }
        }
        setContentView(binding.root)
    }



    private fun moveToDashboard() {
        Intent(this@LoginActivity, HomeActivity::class.java).also { intent ->
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(KEY_EMAIL, viewModel.logInFormState.email)
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewModel() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Users")
        val dataStore = UserPreferences(this)
        val repository = LogInRepository(firebaseAuth, fireStoreReference, dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LogInViewModel::class.java]
    }
}