package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.femi.e_class.*
import com.femi.e_class.databinding.FragmentMyAccountBinding
import com.femi.e_class.viewmodels.HomeActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MyAccountFragment : Fragment() {

    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()
    private lateinit var verifyIdentityDialog: AlertDialog
    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
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

            binding.btnLogOut.setOnClickListener {
                logOut()
            }

            binding.btnDeleteAccount.setOnClickListener {
                deleteAccount()
            }



            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.deleteAccountEvents.collect { event ->
                        binding.progressBar.visible(event is HomeActivityViewModel.DeleteAccountEvent.Loading)
                        binding.btnDeleteAccount.disable(event is HomeActivityViewModel.DeleteAccountEvent.Loading)
                        binding.btnLogOut.disable(event is HomeActivityViewModel.DeleteAccountEvent.Loading)
                        when (event) {
                            is HomeActivityViewModel.DeleteAccountEvent.Success -> {
                                Intent(requireActivity(), MainActivity::class.java).also {
                                    it.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(it)
                                    requireActivity().finish()
                                }
                            }
                            is HomeActivityViewModel.DeleteAccountEvent.Error -> {
                                handleNetworkExceptions(event.exception,
                                    retry = { deleteAccount() })
                            }
                            is HomeActivityViewModel.DeleteAccountEvent.Loading -> {}
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.verifyIdentityEvents.collect { event ->
                        loadingDialog.showLoadingDialog(event is HomeActivityViewModel.VerifyIdentityEvent.Loading)
                        when (event) {
                            is HomeActivityViewModel.VerifyIdentityEvent.Success -> {}
                            is HomeActivityViewModel.VerifyIdentityEvent.Error -> {
                                handleNetworkExceptions(event.exception)
                            }
                            is HomeActivityViewModel.VerifyIdentityEvent.Loading -> {
                            }
                        }
                    }
                }
            }

        }
    }

    private fun logOut() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Hold up")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yup, I'm sure") { _, _ ->
                viewModel.logOut()
                Intent(requireActivity(), MainActivity::class.java).also {
                    it.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                    requireActivity().finish()
                }
            }
            .setNegativeButton("I don't think so") { _, _ -> }
            .show()
    }

    private fun deleteAccount() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Hold up")
            .setMessage("Your account will be PERMANENTLY deleted")
            .setPositiveButton("Yup, proceed") { _, _ ->
                viewModel.deleteAccount()
            }
            .setNegativeButton("No, please cancel") { _, _ -> }
            .show()
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
