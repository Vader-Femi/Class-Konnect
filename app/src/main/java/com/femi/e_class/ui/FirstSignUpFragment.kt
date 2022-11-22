package com.femi.e_class.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.femi.e_class.KEY_EMAIL
import com.femi.e_class.KEY_PASSWORD
import com.femi.e_class.R
import com.femi.e_class.data.User
import com.femi.e_class.databinding.FragmentFirstSignUpBinding
import com.femi.e_class.presentation.RegistrationFormEvent
import com.femi.e_class.viewmodels.SignUpViewModel
import kotlinx.coroutines.launch

class FirstSignUpFragment : Fragment() {

    private var _binding: FragmentFirstSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFirstSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null && context != null) {

            binding.etFirstName.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RegistrationFormEvent.FirstNameChanged(text.toString()))
            }
            binding.etLastName.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RegistrationFormEvent.LastNameChanged(text.toString()))
            }
            binding.etEmail.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RegistrationFormEvent.EmailChanged(text.toString()))
            }
            binding.etMatric.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RegistrationFormEvent.MatricChanged(text.toString()))
            }

            binding.etPassword.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RegistrationFormEvent.PasswordChanged(text.toString()))
            }
            binding.etRetypePassword.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(text.toString()))
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.registrationEvents.collect { event ->
                        binding.progressBar.isVisible = event is SignUpViewModel.RegistrationEvent.Loading
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
                            is SignUpViewModel.RegistrationEvent.Error ->{
                                Toast.makeText(requireContext(),
                                    event.exception?.message.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show()
                            }
                            is SignUpViewModel.RegistrationEvent.Loading -> {
//                                binding.progressBar.isVisible = event is SignUpViewModel.RegistrationEvent.Loading
                            }
                        }
                    }
                }
            }

            binding.btnContinue.setOnClickListener {
                viewModel.onEvent(RegistrationFormEvent.Submit)
                binding.firstNameLayout.helperText = viewModel.registrationFormState.firstNameError
                binding.lastNameLayout.helperText = viewModel.registrationFormState.lastNameError
                binding.emailLayout.helperText = viewModel.registrationFormState.emailError
                binding.matricLayout.helperText = viewModel.registrationFormState.matricError
                binding.passwordLayout.helperText = viewModel.registrationFormState.passwordError
                binding.retypePasswordLayout.helperText = viewModel.registrationFormState.repeatedPasswordError
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}