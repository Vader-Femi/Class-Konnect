package com.femi.e_class.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.femi.e_class.domain.use_case.*
import com.femi.e_class.presentation.*
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
//    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword()
) : BaseViewModel(repository) {

    var roomFormState by mutableStateOf(RoomFormState())

    var updateProfileValidationFormState by mutableStateOf(UpdateProfileFormState())

    private val roomEventChannel = Channel<RoomEvent>()
    val roomEvents = roomEventChannel.receiveAsFlow()

    private val updateProfileValidationEventChannel = Channel<UpdateProfileValidationEvent>()
    val updateProfileValidationEvents = updateProfileValidationEventChannel.receiveAsFlow()

    private val updateProfileEventChannel = Channel<UpdateProfileEvent<Any?>>()
    val updateProfileEvents = updateProfileEventChannel.receiveAsFlow()

    private val deleteAccountEventChannel = Channel<DeleteAccountEvent<Any?>>()
    val deleteAccountEvents = deleteAccountEventChannel.receiveAsFlow()

    private val verifyIdentityEventChannel = Channel<VerifyIdentityEvent<Any?>>()
    val verifyIdentityEvents = verifyIdentityEventChannel.receiveAsFlow()

    fun onEvent(event: RoomFormEvent) {
        when (event) {
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

    fun onEvent(event: DeleteAccountFormEvent) {
        when (event) {
            DeleteAccountFormEvent.Submit -> {
                viewModelScope.launch {
                    deleteAccountEventChannel.send(DeleteAccountEvent.Submit)
                }
            }
        }
    }

    fun onEvent(event: UpdateProfileFormEvent) {
        when (event) {
            is UpdateProfileFormEvent.FirstNameChanged -> {
                updateProfileValidationFormState =
                    updateProfileValidationFormState.copy(firstName = event.firstName)
            }
            is UpdateProfileFormEvent.LastNameChanged -> {
                updateProfileValidationFormState =
                    updateProfileValidationFormState.copy(lastName = event.lastName)
            }
            is UpdateProfileFormEvent.EmailChanged -> {
                updateProfileValidationFormState =
                    updateProfileValidationFormState.copy(email = event.email)
            }
            is UpdateProfileFormEvent.MatricChanged -> {
                updateProfileValidationFormState =
                    updateProfileValidationFormState.copy(matric = event.matric)
            }
            is UpdateProfileFormEvent.Submit -> {
                submitRegistrationData()
            }
        }
    }

    override fun logOut() {
        repository.getAuthReference().signOut()
        super.logOut()
    }
    private fun submitRoomFormData() {
        val courseCodeResults = validateCourseCode.execute(roomFormState.courseCode)
        val roomPasswordResults = validateRoomPassword.execute(roomFormState.roomPassword)

        val hasError = listOf(
            courseCodeResults,
            roomPasswordResults
        ).any { !it.successful }

        roomFormState = roomFormState.copy(
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


        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            matricResult
        ).any { !it.successful }

        updateProfileValidationFormState = updateProfileValidationFormState.copy(
            firstNameError = firstNameResult.errorMessage,
            lastNameError = lastNameResult.errorMessage,
            emailError = emailResult.errorMessage,
            matricError = matricResult.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            updateProfileValidationEventChannel.send(UpdateProfileValidationEvent.Success)
        }
    }

    fun updateUser() {
        val firstName = updateProfileValidationFormState.firstName
        val lastName = updateProfileValidationFormState.lastName
        val matric = updateProfileValidationFormState.matric
        val email = updateProfileValidationFormState.email

        val userHashMap = hashMapOf(
            "FirstName" to firstName,
            "LastName" to lastName,
            "Matric" to matric,
            "Email" to email)

        viewModelScope.launch {
            updateProfileEventChannel.send(UpdateProfileEvent.Loading)
        }
        repository.getAuthReference().currentUser?.updateEmail(email)
            ?.addOnCompleteListener { updateEmailTask ->
                if (updateEmailTask.isSuccessful) {
                    saveUserDetailsFirebase(email,userHashMap)
                } else {
                    viewModelScope.launch {
                        updateProfileEventChannel.send(UpdateProfileEvent.Error(updateEmailTask.exception))
                    }
                }
            }


    }

    private fun saveUserDetailsFirebase(email: String, userHashMap: HashMap<String, String>) {
        repository.getCollectionReference()
            .document(email)
            .set(userHashMap)
            .addOnSuccessListener {
                viewModelScope.launch {
                    saveUserDetailsLocally(userHashMap)
                    updateProfileEventChannel.send(UpdateProfileEvent.Success)
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    updateProfileEventChannel.send(UpdateProfileEvent.Error(it))
                }
            }
    }

    private suspend fun saveUserDetailsLocally(data: HashMap<String, String>?) {
        repository.userFName(data?.get("FirstName") ?: "")
        repository.userLName(data?.get("LastName") ?: "")
        repository.userEmail(data?.get("Email") ?: "")
        repository.userMatric(data?.get("Matric")?.toLong() ?: 0L)
    }

    fun deleteAccount() {
        viewModelScope.launch {
            deleteAccountEventChannel.send(DeleteAccountEvent.Loading)
            repository.getAuthReference()
                .currentUser
                ?.delete()
                ?.addOnCompleteListener { addUserTask ->
                    if (addUserTask.isSuccessful) {
                        deleteFromDatabase()
                    } else{
                        viewModelScope.launch {
                            deleteAccountEventChannel.send(DeleteAccountEvent.Error(addUserTask.exception))
                        }
                    }
                }
        }
    }

    private fun deleteFromDatabase() {
        viewModelScope.launch {
            val email = repository.userEmail()
            repository.getCollectionReference()
                .document(email)
                .delete()
                .addOnSuccessListener {
                    viewModelScope.launch {
                        logOut()
                        deleteAccountEventChannel.send(DeleteAccountEvent.Success)
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        deleteAccountEventChannel.send(DeleteAccountEvent.Error(it))
                    }
                }
        }
    }

    fun verifyIdentity(password: String) {
        viewModelScope.launch {
            val email = repository.userEmail()
            verifyIdentityEventChannel.send(VerifyIdentityEvent.Loading)
            repository.getAuthReference()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { addUserTask ->
                    if (addUserTask.isSuccessful) {
                        viewModelScope.launch {
                            verifyIdentityEventChannel.send(VerifyIdentityEvent.Success)
                        }
                    } else {
                        viewModelScope.launch {
                            verifyIdentityEventChannel.send(VerifyIdentityEvent.Error(addUserTask.exception))
                        }
                    }
                }
        }
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

    sealed class DeleteAccountEvent<out T> {
        object Success : DeleteAccountEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : DeleteAccountEvent<Nothing>()
        object Loading : DeleteAccountEvent<Nothing>()
        object Submit : DeleteAccountEvent<Nothing>()
    }

    sealed class VerifyIdentityEvent<out T> {
        object Success : VerifyIdentityEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : VerifyIdentityEvent<Nothing>()
        object Loading : VerifyIdentityEvent<Nothing>()
    }

}