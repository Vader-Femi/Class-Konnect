package com.femi.e_class.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.lifecycle.lifecycleScope
import com.femi.e_class.*
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.databinding.FragmentUpdateProfileBinding
import com.femi.e_class.presentation.UpdateProfileFormEvent
import com.femi.e_class.viewmodels.HomeActivityViewModel
import kotlinx.coroutines.launch

class UpdateProfileFragment : Fragment() {

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            getDefaults()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val scrollState = rememberScrollState()
                        val context = LocalContext.current
                        val state = viewModel.updateProfileValidationFormState
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(8.dp, 0.dp, 8.dp, 0.dp),
                        ) {
                            var loading by remember { mutableStateOf(false) }
                            LaunchedEffect(key1 = context) {
                                viewModel.updateProfileValidationEvents.collect { event ->
                                    when (event) {
                                        is HomeActivityViewModel.UpdateProfileValidationEvent.Success -> {
                                            viewModel.updateUser()
                                        }
                                    }
                                }
                            }
                            LaunchedEffect(key1 = viewModel.updateProfileEvents) {
                                viewModel.updateProfileEvents.collect { event ->
                                    loading =
                                        (event is HomeActivityViewModel.UpdateProfileEvent.Loading)
                                    when (event) {
                                        is HomeActivityViewModel.UpdateProfileEvent.Success -> {
                                            Toast.makeText(requireContext(),
                                                "Success",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                        is HomeActivityViewModel.UpdateProfileEvent.Error -> {
                                            handleNetworkExceptions(event.exception,
                                                retry = {
                                                    viewModel.onEvent(UpdateProfileFormEvent.Submit)
                                                })
                                        }
                                        is HomeActivityViewModel.UpdateProfileEvent.Loading -> {}
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(80.dp))
                            OutlinedTextField(
                                value = state.firstName,
                                label = { Text(text = "First Name") },
                                onValueChange = {
                                    viewModel.onEvent(UpdateProfileFormEvent.FirstNameChanged(it))
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
                                    viewModel.onEvent(UpdateProfileFormEvent.LastNameChanged(it))
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
                                    viewModel.onEvent(UpdateProfileFormEvent.MatricChanged(it))
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
                                    viewModel.onEvent(UpdateProfileFormEvent.EmailChanged(it))
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
                            if (loading) {
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            Button(
                                onClick = {
                                    viewModel.onEvent(UpdateProfileFormEvent.Submit)
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

    private suspend fun getDefaults(){
        viewModel.onEvent(UpdateProfileFormEvent.FirstNameChanged(viewModel.userFName()))
        viewModel.onEvent(UpdateProfileFormEvent.LastNameChanged(viewModel.userLName()))
        viewModel.onEvent(UpdateProfileFormEvent.MatricChanged(viewModel.userMatric().toString()))
        viewModel.onEvent(UpdateProfileFormEvent.EmailChanged(viewModel.userEmail()))
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}