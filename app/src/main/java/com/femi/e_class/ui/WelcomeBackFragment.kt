package com.femi.e_class.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.femi.e_class.R
import com.femi.e_class.databinding.FragmentWelcomeBackBinding
import com.femi.e_class.viewmodels.HomeActivityViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class WelcomeBackFragment : Fragment() {

    private var _binding: FragmentWelcomeBackBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWelcomeBackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null && context != null) {

            var greeting = "Good Day "
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val calendar = Calendar.getInstance()
                val current = LocalDateTime.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND))
                greeting = when (current.hour){
                    in 6..11 -> "Good Morning "
                    in 12..16 -> "Good Afternoon "
                    in 17..23 -> "Good Night "
                    else -> "You should be sleeping "
                }
            }

            lifecycleScope.launch {
                val greetingText = "${getString(R.string.welcome_back)} ${viewModel.userFName()}"
                binding.toolbarText.text = greetingText
                binding.toolbarLayout.title = "$greeting${viewModel.userFName()}"
            }

            binding.btnStartOrJoin.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeBack_to_classDetails)
            }

            binding.btnUpdateProfile.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeBack_to_updateProfile)
            }

        }
    }
}