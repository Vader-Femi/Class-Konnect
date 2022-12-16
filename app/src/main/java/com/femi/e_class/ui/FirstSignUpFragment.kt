package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.femi.e_class.*
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.data.User
import com.femi.e_class.databinding.FragmentFirstSignUpBinding
import com.femi.e_class.presentation.RegistrationFormEvent
import com.femi.e_class.viewmodels.SignUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FirstSignUpFragment : Fragment() {

    private var _binding: FragmentFirstSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<SignUpViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFirstSignUpBinding.inflate(inflater, container, false)
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val scrollState = rememberScrollState()
                        val coroutineScope = rememberCoroutineScope()
                        val context = LocalContext.current
                        val state = viewModel.registrationFormState
                        var showPassword by remember { mutableStateOf(false) }
                        var accountCreated by remember { mutableStateOf(false) }
                        var loading by remember { mutableStateOf(false) }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(30.dp, 60.dp, 30.dp, 30.dp),
                        ) {
                            LaunchedEffect(key1 = context) {
                                viewModel.validationEvents.collect { event ->
                                    when (event) {
                                        is SignUpViewModel.ValidationEvent.Success -> {
                                            viewModel.signUpUser(User(
                                                firstName = viewModel.registrationFormState.firstName,
                                                lastName = viewModel.registrationFormState.lastName,
                                                email = viewModel.registrationFormState.email,
                                                matric = viewModel.registrationFormState.matric,
                                                password = viewModel.registrationFormState.password
                                            )
                                            )
                                        }
                                    }
                                }
                            }
                            LaunchedEffect(key1 = viewModel.registrationEvents) {
                                viewModel.registrationEvents.collect { event ->
                                    loading = (event is SignUpViewModel.RegistrationEvent.Loading)
                                    when (event) {
                                        is SignUpViewModel.RegistrationEvent.Success -> {
                                            accountCreated = true
                                        }
                                        is SignUpViewModel.RegistrationEvent.Error -> {
                                            handleNetworkExceptions(event.exception,
                                                retry = { viewModel.onEvent(RegistrationFormEvent.Submit) })
                                        }
                                        is SignUpViewModel.RegistrationEvent.Loading -> {}
                                    }
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start),
                                text = "Create your account",
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
                                value = state.firstName,
                                label = { Text(text = "First Name") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.FirstNameChanged(it))
                                },
                                isError = state.firstNameError != null,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                leadingIcon = {
                                    Icon(Icons.Filled.Person, "First Name Icon")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = false,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (state.firstNameError != null) {
                                Text(
                                    text = state.firstNameError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            OutlinedTextField(
                                value = state.lastName,
                                label = { Text(text = "Last Name") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.LastNameChanged(it))
                                },
                                isError = state.lastNameError != null,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                leadingIcon = {
                                    Icon(Icons.Filled.Person, "Last Name Icon")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = false,
                                    imeAction = ImeAction.Next
                                )
                            )
                            if (state.lastNameError != null) {
                                Text(
                                    text = state.lastNameError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            OutlinedTextField(
                                value = state.matric,
                                label = { Text(text = "Matric") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.MatricChanged(it))
                                },
                                isError = state.matricError != null,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                leadingIcon = {
                                    Icon(Icons.Filled.Settings, "Last Name Icon")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.NumberPassword,
                                    capitalization = KeyboardCapitalization.None,
                                    autoCorrect = false,
                                    imeAction = ImeAction.Next
                                ),
                            )
                            if (state.matricError != null) {
                                Text(
                                    text = state.matricError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            OutlinedTextField(
                                value = state.email,
                                label = { Text(text = "Email") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))
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
                                value = state.password,
                                label = { Text(text = "Password") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it))
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
                                    viewModel.onEvent(RegistrationFormEvent.Submit)
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(),
                                enabled = !loading
                            ) {
                                Text(text = "Sign Up")
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            val annotatedText = buildAnnotatedString {
                                pushStringAnnotation(tag = "Sign In Button",
                                    annotation = "Sign In")

                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                    ),
                                ) {
                                    append("Already have an account? ")
                                }

                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                    ),
                                ) {
                                    append("Sign In")
                                }

                                pop()
                            }
                            ClickableText(
                                text = annotatedText,
                                onClick = { offset ->
                                    annotatedText.getStringAnnotations(tag = "Sign In Button",
                                        start = offset,
                                        end = offset)
                                        .firstOrNull()?.let {
                                            startActivity(Intent(activity,
                                                LoginActivity::class.java))
                                            activity?.finish()
                                        }
                                }
                            )
                            if (accountCreated) {
                                AlertDialog(
                                    confirmButton = {
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .padding(0.dp, 30.dp, 0.dp, 0.dp)
                                        .align(Alignment.CenterHorizontally),
                                    text = {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.4f),
                                                painter = painterResource(id = R.drawable.leader_pana),
                                                contentDescription = "Account Created Icon"
                                            )
                                            Text(
                                                text = "Successful!",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                            Text(
                                                text = "Your account is ready to use. You will be redirected to log page in a few seconds",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(0.dp, 20.dp, 0.dp, 30.dp),
                                            )
                                            CircularProgressIndicator()
                                        }
                                    },
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 20.dp,
                                    onDismissRequest = {
                                        proceedToLogin()
                                    }
                                )
                                LaunchedEffect(key1 = accountCreated) {
                                    if (accountCreated) {
                                        coroutineScope.launch {
                                            delay(7.seconds)
                                            proceedToLogin()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }

    private fun proceedToLogin() {
        Intent(activity, LoginActivity::class.java).also { intent ->
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(KEY_EMAIL, viewModel.registrationFormState.email)
            intent.putExtra(KEY_PASSWORD, viewModel.registrationFormState.password)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}