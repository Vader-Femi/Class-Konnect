package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.femi.e_class.R
import com.femi.e_class.compose.E_ClassTheme
import com.femi.e_class.databinding.FragmentMyAccountBinding
import com.femi.e_class.handleNetworkExceptions
import com.femi.e_class.loadingDialog
import com.femi.e_class.presentation.DeleteAccountFormEvent
import com.femi.e_class.presentation.LogOutFormEvent
import com.femi.e_class.showLoadingDialog
import com.femi.e_class.viewmodels.HomeActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
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

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = true) {
                    Surface {
                        val scrollState = rememberScrollState()
                        val context = LocalContext.current
                        LaunchedEffect(key1 = viewModel.deleteAccountEvents){
                            viewModel.deleteAccountEvents.collect { event ->
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
                                    else -> {

                                    }
                                }
                            }
                        }
                        LaunchedEffect(key1 = viewModel.verifyIdentityEvents){
                            viewLifecycleOwner.lifecycleScope.launch {
                                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                    viewModel.verifyIdentityEvents.collect { event ->
                                        loadingDialog.showLoadingDialog(event is HomeActivityViewModel.VerifyIdentityEvent.Loading)
                                        when (event) {
                                            is HomeActivityViewModel.VerifyIdentityEvent.Success -> {
                                                viewModel.deleteAccount()
                                            }
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize(),
                        ) {
                            Button(onClick = {
                                logOut()
                                viewModel.onEvent(DeleteAccountFormEvent.Submit)
                            }) {
                                Text(text = "Log Out")
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedButton(onClick = {
                                deleteAccount()
                            }) {
                                Text(text = "Delete Account")
                            }
                        }
                    }
                }

            }
        }

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
                verifyIdentity()
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
                if (!password.isNullOrEmpty())
                    viewModel.verifyIdentity(password)
            }
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setCancelable(false)
            .show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
