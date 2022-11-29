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
import com.femi.e_class.databinding.FragmentUpdateProfileBinding
import com.femi.e_class.disable
import com.femi.e_class.presentation.UpdateProfileFormEvent
import com.femi.e_class.viewmodels.HomeActivityViewModel
import com.femi.e_class.visible
import kotlinx.coroutines.launch

class UpdateProfileFragment : Fragment() {

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null && context != null) {

            binding.etFirstName.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(UpdateProfileFormEvent.FirstNameChanged(text.toString()))
            }
            binding.etLastName.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(UpdateProfileFormEvent.LastNameChanged(text.toString()))
            }
            binding.etEmail.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(UpdateProfileFormEvent.EmailChanged(text.toString()))
            }
            binding.etMatric.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(UpdateProfileFormEvent.MatricChanged(text.toString()))
            }

            binding.etPassword.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(UpdateProfileFormEvent.PasswordChanged(text.toString()))
            }
            binding.etRetypePassword.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(UpdateProfileFormEvent.RepeatedPasswordChanged(text.toString()))
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.updateProfileValidationEvents.collect { event ->
                        when (event) {
                            is HomeActivityViewModel.UpdateProfileValidationEvent.Success -> {
                                viewModel.updateUser(
                                    firstName = viewModel.updateProfileValidationFormState.firstName,
                                    lastName = viewModel.updateProfileValidationFormState.lastName,
                                    email = viewModel.updateProfileValidationFormState.email,
                                    matric = viewModel.updateProfileValidationFormState.matric,
                                    password = viewModel.updateProfileValidationFormState.password
                                )
                            }
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.updateProfileEvents.collect { event ->
                        binding.progressBar.visible(event is HomeActivityViewModel.UpdateProfileEvent.Loading)
                        binding.btnUpdateProfile.disable(event is HomeActivityViewModel.UpdateProfileEvent.Loading)
                        when (event) {
                            is HomeActivityViewModel.UpdateProfileEvent.Success -> {
                                findNavController().popBackStack()
                            }
                            is HomeActivityViewModel.UpdateProfileEvent.Error ->{
                                Toast.makeText(requireContext(),
                                    event.exception?.message.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show()
                            }
                            is HomeActivityViewModel.UpdateProfileEvent.Loading -> {
                            }
                        }
                    }
                }
            }

            binding.btnUpdateProfile.setOnClickListener {
                viewModel.onEvent(UpdateProfileFormEvent.Submit)
                binding.firstNameLayout.helperText = viewModel.updateProfileValidationFormState.firstNameError
                binding.lastNameLayout.helperText = viewModel.updateProfileValidationFormState.lastNameError
                binding.emailLayout.helperText = viewModel.updateProfileValidationFormState.emailError
                binding.matricLayout.helperText = viewModel.updateProfileValidationFormState.matricError
                binding.passwordLayout.helperText = viewModel.updateProfileValidationFormState.passwordError
                binding.retypePasswordLayout.helperText = viewModel.updateProfileValidationFormState.repeatedPasswordError
            }

        }
    }
}