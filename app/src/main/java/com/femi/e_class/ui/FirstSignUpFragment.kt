package com.femi.e_class.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.femi.e_class.KEY_EMAIL
import com.femi.e_class.R
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

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.validationEvents.collect { event ->
                        when (event) {
                            SignUpViewModel.ValidationEvent.Success -> {
                                viewModel.saveUser(
                                    firstName = viewModel.registrationFormState.firstName,
                                    lastName = viewModel.registrationFormState.lastName,
                                    email = viewModel.registrationFormState.email,
                                    matric = viewModel.registrationFormState.matric
                                )
                            }
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.registrationEvents.collect { event ->
                        when (event) {
                            SignUpViewModel.RegistrationEvent.Success -> {
                                findNavController().navigate(R.id.action_first_to_second_sign_up,
                                    Bundle().apply {
                                        putString(KEY_EMAIL,
                                            viewModel.registrationFormState.email)
                                    })
                            }
                            SignUpViewModel.RegistrationEvent.Failed -> {
                                Toast.makeText(requireContext(),
                                    getString(R.string.register_error_message),
                                    Toast.LENGTH_SHORT)
                                    .show()
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
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}