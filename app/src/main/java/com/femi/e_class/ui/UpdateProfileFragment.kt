package com.femi.e_class.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.femi.e_class.*
import com.femi.e_class.databinding.FragmentUpdateProfileBinding
import com.femi.e_class.presentation.UpdateProfileFormEvent
import com.femi.e_class.viewmodels.HomeActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class UpdateProfileFragment : Fragment() {

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()
    private lateinit var verifyIdentityDialog: AlertDialog
    private lateinit var loadingDialog: AlertDialog

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

            val loadingView = layoutInflater.inflate(R.layout.loading, null)
            loadingDialog = requireContext().loadingDialog(
                view = loadingView,
                title = getString(R.string.verify_identity),
                message = "Logging In...Please Wait")
            verifyIdentity()

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
//            binding.etRetypePassword.doOnTextChanged { text, _, _, _ ->
//                viewModel.onEvent(UpdateProfileFormEvent.RepeatedPasswordChanged(text.toString()))
//            }

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
                            is HomeActivityViewModel.UpdateProfileEvent.Error -> {
                                handleNetworkExceptions(event.exception, retry = {attemptUpdateProfile()})
                            }
                            is HomeActivityViewModel.UpdateProfileEvent.Loading -> {
                            }
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.verifyIdentityEvents.collect { event ->
                        loadingDialog.showLoadingDialog(event is HomeActivityViewModel.VerifyIdentityEvent.Loading)
                        when (event) {
                            is HomeActivityViewModel.VerifyIdentityEvent.Success -> {
                                fillProfileFields()
                            }
                            is HomeActivityViewModel.VerifyIdentityEvent.Error -> {
                                Toast.makeText(requireContext(),
                                    event.exception?.message.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show()
                                verifyIdentityDialog.show()
                            }
                            is HomeActivityViewModel.VerifyIdentityEvent.Loading -> {}
                        }
                    }
                }
            }

            binding.btnUpdateProfile.setOnClickListener {
                attemptUpdateProfile()
            }

        }
    }

    private fun attemptUpdateProfile(){
        viewModel.onEvent(UpdateProfileFormEvent.Submit)
        binding.firstNameLayout.helperText =
            viewModel.updateProfileValidationFormState.firstNameError
        binding.lastNameLayout.helperText =
            viewModel.updateProfileValidationFormState.lastNameError
        binding.emailLayout.helperText =
            viewModel.updateProfileValidationFormState.emailError
        binding.matricLayout.helperText =
            viewModel.updateProfileValidationFormState.matricError
        binding.passwordLayout.helperText =
            viewModel.updateProfileValidationFormState.passwordError
//                binding.retypePasswordLayout.helperText =
//                    viewModel.updateProfileValidationFormState.repeatedPasswordError
    }

    private fun fillProfileFields() {
        lifecycleScope.launch {
            binding.etEmail.setText(viewModel.userEmail())
            binding.etFirstName.setText(viewModel.userFName())
            binding.etLastName.setText(viewModel.userLName())
            binding.etMatric.setText(viewModel.userMatric().toString())
        }

    }

    private fun verifyIdentity() {
        val verifyIdentityAlertDialogView = LayoutInflater.from(requireActivity())
            .inflate(R.layout.verify_identity, null, false)
        val passwordField =
            verifyIdentityAlertDialogView.findViewById<TextInputEditText>(R.id.etIdentityPassword)

        verifyIdentityDialog = MaterialAlertDialogBuilder(requireActivity())
            .setView(verifyIdentityAlertDialogView)
            .setTitle(getString(R.string.verify_identity))
            .setMessage(getString(R.string.verify_identity_text))
            .setPositiveButton("Verify") { _, _ ->
                val password = passwordField.text?.toString()
                if (password.isNullOrEmpty())
                    findNavController().popBackStack()
                else
                    viewModel.verifyIdentity(password)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                findNavController().popBackStack()
            }
            .setCancelable(false)
            .show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}