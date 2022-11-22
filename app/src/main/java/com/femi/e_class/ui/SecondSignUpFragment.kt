package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.femi.e_class.KEY_EMAIL
import com.femi.e_class.KEY_PASSWORD
import com.femi.e_class.databinding.FragmentSecondSignUpBinding
import com.femi.e_class.viewmodels.SignUpViewModel

class SecondSignUpFragment : Fragment() {

    private var _binding: FragmentSecondSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<SignUpViewModel>()

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

            (requireActivity() as SignUpActivity ).supportActionBar?.hide()

            val email = requireArguments().getString(KEY_EMAIL)
            val password = requireArguments().getString(KEY_PASSWORD)

            binding.btnSignIn.setOnClickListener {
                if (activity != null) {
                    Intent(activity, LoginActivity::class.java).also { intent ->
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra(KEY_EMAIL, email)
                        intent.putExtra(KEY_PASSWORD, password)
                        startActivity(intent)
                        requireActivity().finish()
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