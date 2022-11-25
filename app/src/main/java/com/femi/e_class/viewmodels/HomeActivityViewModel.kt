package com.femi.e_class.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.femi.e_class.domain.use_case.ValidateCourseCode
import com.femi.e_class.domain.use_case.ValidatePassword
import com.femi.e_class.domain.use_case.ValidateRoomName
import com.femi.e_class.presentation.RoomFormEvent
import com.femi.e_class.presentation.RoomFormState
import com.femi.e_class.repositories.HomeActivityRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeActivityViewModel(
    repository: HomeActivityRepository,
//    private val validateRoomName: ValidateRoomName = ValidateRoomName(),
    private val validateCourseCode: ValidateCourseCode = ValidateCourseCode(),
    private val validatePassword: ValidatePassword = ValidatePassword()
) : BaseViewModel(repository) {

    var roomFormState by mutableStateOf(RoomFormState())

    private val roomEventChannel = Channel<RoomEvent>()
    val roomEvents = roomEventChannel.receiveAsFlow()

    fun onEvent(event: RoomFormEvent) {
        when (event) {
//            is RoomFormEvent.RoomNameChanged -> {
//                roomFormState = roomFormState.copy(roomName = event.roomName)
//            }
            is RoomFormEvent.CourseCodeChanged -> {
                roomFormState = roomFormState.copy(courseCode = event.courseCode)
            }
            is RoomFormEvent.PasswordChanged -> {
                roomFormState = roomFormState.copy(password = event.password)
            }
            is RoomFormEvent.Submit -> {
                submitRoomFormData()
            }
        }
    }

    private fun submitRoomFormData() {
//        val roomNameResults = validateRoomName.execute(roomFormState.roomName)
        val courseCodeResults = validateCourseCode.execute(roomFormState.courseCode)
        val passwordResults = validatePassword.execute(roomFormState.password)

        val hasError = listOf(
//            roomNameResults,
            courseCodeResults,
            passwordResults
        ).any { !it.successful }

        roomFormState = roomFormState.copy(
//            roomNameError = roomNameResults.errorMessage,
            courseCodeError = courseCodeResults.errorMessage,
            passwordError = passwordResults.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            roomEventChannel.send(RoomEvent.Success)
        }
    }

    sealed class RoomEvent {
        object Success : RoomEvent()
    }

}