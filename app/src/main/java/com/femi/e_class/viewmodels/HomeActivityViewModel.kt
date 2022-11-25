package com.femi.e_class.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.femi.e_class.domain.use_case.ValidateCourseCode
import com.femi.e_class.domain.use_case.ValidateRoomName
import com.femi.e_class.presentation.RoomFormEvent
import com.femi.e_class.presentation.RoomFormState
import com.femi.e_class.repositories.HomeActivityRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeActivityViewModel(
    private val repository: HomeActivityRepository,
    private val validateRoomName: ValidateRoomName = ValidateRoomName(),
    private val validateCourseCode: ValidateCourseCode = ValidateCourseCode()
) : BaseViewModel() {

    private var _email = MutableLiveData("")
    var email: String
        get() = _email.value ?: ""
        set(value) {
            _email.value = value
        }

    private var _firstName = MutableLiveData("")
    var firstName: String
        get() = _firstName.value ?: ""
        set(value) {
            _firstName.value = value
        }

    private var _matric = MutableLiveData("")
    var matric: String
        get() = _matric.value ?: ""
        set(value) {
            _matric.value = value
        }

    var roomFormState by mutableStateOf(RoomFormState())

    private val roomEventChannel = Channel<RoomEvent>()
    val roomEvents = roomEventChannel.receiveAsFlow()

    private val getUserEventChannel = Channel<GetUserEvent<Any?>>()
    val getUserEvent = getUserEventChannel.receiveAsFlow()

    fun onEvent(event: RoomFormEvent) {
        when (event) {
            is RoomFormEvent.RoomNameChanged -> {
                roomFormState = roomFormState.copy(roomName = event.roomName)
            }
            is RoomFormEvent.CourseCodeChanged -> {
                roomFormState = roomFormState.copy(courseCode = event.courseCode)
            }
            is RoomFormEvent.Submit -> {
                submitRoomFormData()
            }
        }
    }

    private fun submitRoomFormData() {
        val roomNameResults = validateRoomName.execute(roomFormState.roomName)
        val courseCodeResults = validateCourseCode.execute(roomFormState.courseCode)

        val hasError = listOf(
            roomNameResults,
            courseCodeResults
        ).any { !it.successful }

        roomFormState = roomFormState.copy(
            roomNameError = roomNameResults.errorMessage,
            courseCodeError = courseCodeResults.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            roomEventChannel.send(RoomEvent.Success)
        }
    }


    fun getUser(
        email: String,
    ) {
        viewModelScope.launch {
            getUserEventChannel.send(GetUserEvent.Loading)
        }
        repository.getCollectionReference()
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null)
                    saveUserDetails(document.data)
                viewModelScope.launch {
                    getUserEventChannel.send(GetUserEvent.Success)
                }
            }.addOnFailureListener { exception ->
                viewModelScope.launch {
                    getUserEventChannel.send(GetUserEvent.Error(exception))
                }
            }
    }

    private fun saveUserDetails(data: MutableMap<String, Any>?) {
        _firstName.value = data?.get("FirstName")?.toString() ?: ""
        _email.value = data?.get("Email")?.toString() ?: ""
        _matric.value = data?.get("Matric")?.toString() ?: ""
    }

    sealed class RoomEvent {
        object Success : RoomEvent()
    }

    sealed class GetUserEvent<out T> {
        object Success : GetUserEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?): GetUserEvent<Nothing>()
        object Loading : GetUserEvent<Nothing>()
    }

}