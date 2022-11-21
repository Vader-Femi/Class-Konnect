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
import com.femi.e_class.KEY_EMAIL
import com.femi.e_class.R
import com.femi.e_class.databinding.FragmentSecondSignUpBinding
import com.femi.e_class.presentation.SetPasswordFormEvent
import com.femi.e_class.viewmodels.SignUpViewModel
import kotlinx.coroutines.launch

class SecondSignUpFragment : Fragment() {

    private var _binding: FragmentSecondSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<SignUpViewModel>()
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSecondSignUpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null && context != null) {
            email = requireArguments().getString(KEY_EMAIL, "")

            binding.etPassword.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(SetPasswordFormEvent.PasswordChanged(text.toString()))
            }
            binding.etRetypePassword.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(SetPasswordFormEvent.RepeatedPasswordChanged(text.toString()))
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.validationEvents.collect { event ->
                        when (event) {
                            SignUpViewModel.ValidationEvent.Success -> {
                                viewModel.signUpUser(
                                    email,
                                    viewModel.setPasswordFormState.password
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

                                Toast.makeText(requireContext(),
                                    "Success",
                                    Toast.LENGTH_SHORT)
                                    .show()

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

            binding.btnSignUp.setOnClickListener {
                viewModel.onEvent(SetPasswordFormEvent.Submit)
                binding.passwordLayout.helperText = viewModel.setPasswordFormState.passwordError
                binding.retypePasswordLayout.helperText =
                    viewModel.setPasswordFormState.repeatedPasswordError
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}