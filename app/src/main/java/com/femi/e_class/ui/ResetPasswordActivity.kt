package com.femi.e_class.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.data.UserPreferences
import com.femi.e_class.databinding.ActivityResetPasswordBinding
import com.femi.e_class.handleNetworkExceptions
import com.femi.e_class.presentation.ResetPasswordFormEvent
import com.femi.e_class.repositories.ResetPasswordRepository
import com.femi.e_class.viewmodels.ResetPasswordViewModel
import com.femi.e_class.viewmodels.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var viewModel: ResetPasswordViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setupViewModel()
        setContentView(binding.root)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val scrollState = rememberScrollState()
                        val context = LocalContext.current
                        val state = viewModel.resetPasswordFormState
                        var loading by remember { mutableStateOf(false) }
                        var linkSent by remember { mutableStateOf(false) }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(30.dp, 60.dp, 30.dp, 30.dp),
                        ) {
                            LaunchedEffect(key1 = context) {
                                viewModel.validationEvents.collect { event ->
                                    when (event) {
                                        is ResetPasswordViewModel.ValidationEvent.Success -> {
                                            viewModel.sendResetPassword(
                                                email = viewModel.resetPasswordFormState.email
                                            )
                                        }
                                    }
                                }
                            }
                            LaunchedEffect(key1 = state) {
                                viewModel.resetPasswordEvents.collect { event ->
                                    loading =
                                        (event is ResetPasswordViewModel.ResetPasswordEvent.Loading)
                                    when (event) {
                                        is ResetPasswordViewModel.ResetPasswordEvent.Success -> {
                                            linkSent = true
                                        }
                                        is ResetPasswordViewModel.ResetPasswordEvent.Error -> {
                                            handleNetworkExceptions(event.exception,
                                                retry = { viewModel.onEvent(ResetPasswordFormEvent.Submit) })
                                        }
                                        is ResetPasswordViewModel.ResetPasswordEvent.Loading -> {}
                                    }
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start),
                                text = "Reset Password",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start),
                                text = "Enter your password to receive reset lint",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            OutlinedTextField(
                                value = state.email,
                                label = { Text(text = "Email") },
                                onValueChange = {
                                    viewModel.onEvent(ResetPasswordFormEvent.EmailChanged(it))
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
                            if (loading) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                            Button(
                                onClick = {
                                    viewModel.onEvent(ResetPasswordFormEvent.Submit)
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(),
                                enabled = !loading
                            ) {
                                Text(text = "Sent Link")
                            }
                            if (linkSent) {
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
                                                painter = painterResource(id = R.drawable.inbox_cleanup_amico),
                                                contentDescription = "Check Inbox Icon"
                                            )
                                            Text(
                                                text = "Check your inbox",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                            Text(
                                                text = "A link has just been sent to your email. Please check your inbox and use it to reset your password",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(0.dp, 20.dp, 0.dp, 30.dp),
                                            )
                                            Button(
                                                onClick = { finish() },
                                            ) {
                                                Text(
                                                    text = "Return to login",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                )
                                            }
                                        }
                                    },
                                    tonalElevation = 20.dp,
                                    onDismissRequest = {
                                        finish()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataStore = UserPreferences(this)
        val repository = ResetPasswordRepository(firebaseAuth, dataStore)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ResetPasswordViewModel::class.java]
    }
}