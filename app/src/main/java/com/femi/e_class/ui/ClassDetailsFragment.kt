package com.femi.e_class.ui

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
import androidx.navigation.fragment.findNavController
import com.femi.e_class.*
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

            binding.etRoomName.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(RoomFormEvent.RoomNameChanged(text.toString()))
            }
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
                binding.roomNameLayout.helperText = viewModel.roomFormState.roomNameError
                binding.courseCodeLayout.helperText = viewModel.roomFormState.courseCodeError
                binding.passwordLayout.helperText = viewModel.roomFormState.passwordError
            }
        }
    }

    private fun moveToCall() {
        findNavController().navigate(R.id.action_classDetails_to_videoScreen,
            Bundle().apply {
                putString(KEY_ROOM_NAME,
                    viewModel.roomFormState.roomName.trim())
                putString(KEY_COURSE_CODE,
                    viewModel.roomFormState.courseCode.uppercase().trim())
                putString(KEY_PASSWORD,
                    viewModel.roomFormState.password.trim())
                putString(KEY_AVATAR_URL,
                     "")
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}