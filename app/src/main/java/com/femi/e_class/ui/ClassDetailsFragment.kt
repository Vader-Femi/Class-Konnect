package com.femi.e_class.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.femi.e_class.KEY_COURSE_CODE
import com.femi.e_class.KEY_PASSWORD
import com.femi.e_class.databinding.FragmentClassDetailsBinding
import com.femi.e_class.presentation.RoomFormEvent
import com.femi.e_class.viewmodels.HomeActivityViewModel
import kotlinx.coroutines.launch

class ClassDetailsFragment : Fragment() {

    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null && context != null) {

//            binding.etRoomName.doOnTextChanged { text, _, _, _ ->
//                viewModel.onEvent(RoomFormEvent.RoomNameChanged(text.toString()))
//            }
            binding.etCourseCode.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RoomFormEvent.CourseCodeChanged(text.toString()))
            }
            binding.etPassword.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RoomFormEvent.PasswordChanged(text.toString()))
            }


            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.roomEvents.collect { event ->
                        when (event) {
                            is HomeActivityViewModel.RoomEvent.Success -> {
                                moveToCall()
                            }
                        }
                    }
                }
            }

            binding.btnJoin.setOnClickListener {
                viewModel.onEvent(RoomFormEvent.Submit)
//                binding.roomNameLayout.helperText = viewModel.roomFormState.roomNameError
                binding.courseCodeLayout.helperText = viewModel.roomFormState.courseCodeError
                binding.passwordLayout.helperText = viewModel.roomFormState.passwordError
            }
        }
    }

    private fun moveToCall() {
        Intent(activity, VideoActivity::class.java).also { intent ->
            intent.putExtra(KEY_COURSE_CODE,
                viewModel.roomFormState.courseCode.uppercase().trim())
            intent.putExtra(KEY_PASSWORD,
                viewModel.roomFormState.password.trim())
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}