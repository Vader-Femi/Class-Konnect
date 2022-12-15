package com.femi.e_class.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.femi.e_class.*
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.data.User
import com.femi.e_class.databinding.FragmentFirstSignUpBinding
import com.femi.e_class.presentation.RegistrationFormEvent
import com.femi.e_class.viewmodels.SignUpViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

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
                        val context = LocalContext.current
                        val state = viewModel.registrationFormState
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(8.dp, 0.dp, 8.dp, 0.dp),
                        ) {
                            var loading by remember { mutableStateOf(false)}
                            LaunchedEffect(key1 = context){
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
                            LaunchedEffect(key1 = viewModel.registrationEvents){
                                viewModel.registrationEvents.collect { event ->
                                    loading = (event is SignUpViewModel.RegistrationEvent.Loading)
                                    when (event) {
                                        is SignUpViewModel.RegistrationEvent.Success -> {
                                            findNavController().navigate(R.id.action_first_to_second_sign_up,
                                                Bundle().apply {
                                                    putString(KEY_EMAIL,
                                                        viewModel.registrationFormState.email)
                                                    putString(KEY_PASSWORD,
                                                        viewModel.registrationFormState.password)
                                                })
                                        }
                                        is SignUpViewModel.RegistrationEvent.Error -> {
                                            handleNetworkExceptions(event.exception,
                                                retry = { viewModel.onEvent(RegistrationFormEvent.Submit) })
                                        }
                                        is SignUpViewModel.RegistrationEvent.Loading -> {}
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(80.dp))
                            OutlinedTextField(
                                value = state.firstName,
                                label = { Text(text = "First Name") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.FirstNameChanged(it))
                                },
                                isError = state.firstNameError != null,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text
                                ),
                            )
                            if (state.firstNameError != null) {
                                Text(
                                    text = state.firstNameError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = state.lastName,
                                label = { Text(text = "Last Name") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.LastNameChanged(it))
                                },
                                isError = state.lastNameError != null,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text
                                )
                            )
                            if (state.lastNameError != null) {
                                Text(
                                    text = state.lastNameError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = state.matric,
                                label = { Text(text = "Matric") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.MatricChanged(it))
                                },
                                isError = state.matricError != null,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.NumberPassword
                                )
                            )
                            if (state.matricError != null) {
                                Text(
                                    text = state.matricError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = state.email,
                                label = { Text(text = "Email") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))
                                },
                                isError = state.emailError != null,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email
                                )
                            )
                            if (state.emailError != null) {
                                Text(
                                    text = state.emailError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = state.password,
                                label = { Text(text = "Password") },
                                onValueChange = {
                                    viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it))
                                },
                                isError = state.passwordError != null,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password
                                )
                            )
                            if (state.passwordError != null) {
                                Text(
                                    text = state.passwordError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            if (loading) {
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
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
                        }
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}