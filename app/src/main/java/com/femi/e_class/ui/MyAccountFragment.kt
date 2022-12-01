package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.femi.e_class.databinding.FragmentMyAccountBinding
import com.femi.e_class.disable
import com.femi.e_class.viewmodels.HomeActivityViewModel
import com.femi.e_class.visible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class MyAccountFragment : Fragment() {

    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()

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

            binding.btnLogOut.setOnClickListener {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Hey There")
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
                    .setNegativeButton("I don't think so") { _, _ ->}
                    .show()
            }

            binding.btnDeleteAccount.setOnClickListener {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Hold up")
                    .setMessage("Your account will be PERMANENTLY deleted")
                    .setPositiveButton("Yup, proceed") { _, _ ->
                        viewModel.deleteAccount()
                    }
                    .setNegativeButton("No, please cancel") { _, _ ->}
                    .show()
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
                            is HomeActivityViewModel.DeleteAccountEvent.Error ->{
                                Toast.makeText(requireContext(),
                                    event.exception?.message.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show()
                            }
                            is HomeActivityViewModel.DeleteAccountEvent.Loading -> {
                            }
                        }
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
