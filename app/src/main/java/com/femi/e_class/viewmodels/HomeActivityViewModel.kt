package com.femi.e_class.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.femi.e_class.domain.use_case.*
import com.femi.e_class.presentation.UpdateProfileFormEvent
import com.femi.e_class.presentation.UpdateProfileFormState
import com.femi.e_class.presentation.RoomFormEvent
import com.femi.e_class.presentation.RoomFormState
import com.femi.e_class.repositories.HomeActivityRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeActivityViewModel(
    private val repository: HomeActivityRepository,
//    private val validateRoomName: ValidateRoomName = ValidateRoomName(),
    private val validateCourseCode: ValidateCourseCode = ValidateCourseCode(),
    private val validateRoomPassword: ValidateRoomPassword = ValidateRoomPassword(),
    private val validateFirstName: ValidateName = ValidateName(),
    private val validateLastName: ValidateName = ValidateName(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validateMatric: ValidateMatric = ValidateMatric(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword()
) : BaseViewModel(repository) {

    var roomFormState by mutableStateOf(RoomFormState())

    var updateProfileValidationFormState by mutableStateOf(UpdateProfileFormState())

    private val roomEventChannel = Channel<RoomEvent>()
    val roomEvents = roomEventChannel.receiveAsFlow()

    private val updateProfileValidationEventChannel = Channel<UpdateProfileValidationEvent>()
    val updateProfileValidationEvents = updateProfileValidationEventChannel.receiveAsFlow()

    private val updateProfileEventChannel = Channel<UpdateProfileEvent<Any?>>()
    val updateProfileEvents = updateProfileEventChannel.receiveAsFlow()

    fun onEvent(event: RoomFormEvent) {
        when (event) {
//            is RoomFormEvent.RoomNameChanged -> {
//                roomFormState = roomFormState.copy(roomName = event.roomName)
//            }
            is RoomFormEvent.CourseCodeChanged -> {
                roomFormState = roomFormState.copy(courseCode = event.courseCode)
            }
            is RoomFormEvent.RoomPasswordChanged -> {
                roomFormState = roomFormState.copy(roomPassword = event.roomPassword)
            }
            is RoomFormEvent.Submit -> {
                submitRoomFormData()
            }
        }
    }

    fun onEvent(event: UpdateProfileFormEvent) {
        when (event) {
            is UpdateProfileFormEvent.FirstNameChanged -> {
                updateProfileValidationFormState = updateProfileValidationFormState.copy(firstName = event.firstName)
            }
            is UpdateProfileFormEvent.LastNameChanged -> {
                updateProfileValidationFormState = updateProfileValidationFormState.copy(lastName = event.lastName)
            }
            is UpdateProfileFormEvent.EmailChanged -> {
                updateProfileValidationFormState = updateProfileValidationFormState.copy(email = event.email)
            }
            is UpdateProfileFormEvent.MatricChanged -> {
                updateProfileValidationFormState = updateProfileValidationFormState.copy(matric = event.matric)
            }
            is UpdateProfileFormEvent.PasswordChanged -> {
                updateProfileValidationFormState = updateProfileValidationFormState.copy(password = event.password)
            }
            is UpdateProfileFormEvent.RepeatedPasswordChanged -> {
                updateProfileValidationFormState =
                    updateProfileValidationFormState.copy(repeatedPassword = event.repeatedPassword)
            }
            is UpdateProfileFormEvent.Submit -> {
                submitRegistrationData()
            }
        }
    }

    private fun submitRoomFormData() {
//        val roomNameResults = validateRoomName.execute(roomFormState.roomName)
        val courseCodeResults = validateCourseCode.execute(roomFormState.courseCode)
        val roomPasswordResults = validateRoomPassword.execute(roomFormState.roomPassword)

        val hasError = listOf(
//            roomNameResults,
            courseCodeResults,
            roomPasswordResults
        ).any { !it.successful }

        roomFormState = roomFormState.copy(
//            roomNameError = roomNameResults.errorMessage,
            courseCodeError = courseCodeResults.errorMessage,
            roomPasswordError = roomPasswordResults.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            roomEventChannel.send(RoomEvent.Success)
        }
    }

    private fun submitRegistrationData() {
        val firstNameResult = validateFirstName.execute(updateProfileValidationFormState.firstName)
        val lastNameResult = validateLastName.execute(updateProfileValidationFormState.lastName)
        val emailResult = validateEmail.execute(updateProfileValidationFormState.email)
        val matricResult = validateMatric.execute(updateProfileValidationFormState.matric)
        val passwordResult = validatePassword.execute(updateProfileValidationFormState.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            updateProfileValidationFormState.password,
            updateProfileValidationFormState.repeatedPassword)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            matricResult,
            passwordResult,
            repeatedPasswordResult
        ).any { !it.successful }

        updateProfileValidationFormState = updateProfileValidationFormState.copy(
            firstNameError = firstNameResult.errorMessage,
            lastNameError = lastNameResult.errorMessage,
            emailError = emailResult.errorMessage,
            matricError = matricResult.errorMessage,
            passwordError = passwordResult.errorMessage,
            repeatedPasswordError = repeatedPasswordResult.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            updateProfileValidationEventChannel.send(UpdateProfileValidationEvent.Success)
        }
    }

    fun updateUser(
        firstName: String,
        lastName: String,
        matric: String,
        email: String,
        password: String
    ) {
        val userHashMap = hashMapOf(
            "FirstName" to firstName,
            "LastName" to lastName,
            "Matric" to matric,
            "Email" to email,
            "Password" to password)

        viewModelScope.launch {
            updateProfileEventChannel.send(UpdateProfileEvent.Loading)
        }
        repository.getCollectionReference()
            .document(email)
            .set(userHashMap)
            .addOnSuccessListener {
                viewModelScope.launch {
                    saveUserDetails(userHashMap)
                    updateProfileEventChannel.send(UpdateProfileEvent.Success)
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    updateProfileEventChannel.send(UpdateProfileEvent.Error(it))
                }
            }
    }

    private suspend fun saveUserDetails(data: HashMap<String, String>?) {
        repository.userFName(data?.get("FirstName") ?: "")
        repository.userLName(data?.get("LastName") ?: "")
        repository.userEmail(data?.get("Email") ?: "")
        repository.userMatric(data?.get("Matric")?.toLong() ?: 0L)
    }

    sealed class RoomEvent {
        object Success : RoomEvent()
    }

    sealed class UpdateProfileValidationEvent {
        object Success : UpdateProfileValidationEvent()
    }

    sealed class UpdateProfileEvent<out T> {
        object Success : UpdateProfileEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : UpdateProfileEvent<Nothing>()
        object Loading : UpdateProfileEvent<Nothing>()
    }

}