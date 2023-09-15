package com.femi.e_class.viewmodels

import androidx.lifecycle.viewModelScope
import com.femi.e_class.domain.use_case.*
import com.femi.e_class.data.repository.user_activity.UserActivityRepositoryImpl
import com.femi.e_class.presentation.user.home.RoomFormEvent
import com.femi.e_class.presentation.user.home.RoomFormState
import com.femi.e_class.presentation.user.profile.UpdateProfileFormEvent
import com.femi.e_class.presentation.user.profile.UpdateProfileFormState
import com.femi.e_class.presentation.user.settings.VerifyIdentityFormEvent
import com.femi.e_class.presentation.user.settings.VerifyIdentityFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserActivityRepositoryImpl,
    private val validateCourseCode: ValidateCourseCode,
    private val validateLoginPassword: ValidateLogInPassword,
    private val validateFirstName: ValidateName,
    private val validateLastName: ValidateName,
    private val validateEmail: ValidateEmail,
    private val validateMatric: ValidateMatricOrId,
) : BaseViewModel(repository) {

    var roomFormState = MutableStateFlow(RoomFormState()) //mutableStateOf(RoomFormState())

    var updateProfileValidationFormState = MutableStateFlow(UpdateProfileFormState())

    var verifyIdentityFormState = MutableStateFlow(VerifyIdentityFormState())

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
                roomFormState.update {
                    it.copy(
                        courseCode = event.courseCode,
                        courseCodeError = null
                    )
                }
            }

            is RoomFormEvent.Submit -> {
                submitRoomFormData()
            }
        }
    }

    fun onEvent(event: VerifyIdentityFormEvent) {
        when (event) {
            is VerifyIdentityFormEvent.PasswordChanged -> {
                verifyIdentityFormState.update {
                    it.copy(
                        password = event.password,
                        passwordError = null
                    )
                }
            }

            is VerifyIdentityFormEvent.Submit -> {
                verifyIdentity()
            }
        }
    }

    fun onEvent(event: UpdateProfileFormEvent) {
        when (event) {
            is UpdateProfileFormEvent.FirstNameChanged -> {
                updateProfileValidationFormState.update {
                    it.copy(firstName = event.firstName, firstNameError = null)
                }
            }

            is UpdateProfileFormEvent.LastNameChanged -> {
                updateProfileValidationFormState.update {
                    it.copy(lastName = event.lastName, lastNameError = null)
                }
            }

            is UpdateProfileFormEvent.EmailChanged -> {
                updateProfileValidationFormState.update {
                    it.copy(email = event.email, emailError = null)
                }
            }

            is UpdateProfileFormEvent.MatricChanged -> {
                updateProfileValidationFormState.update {
                    it.copy(matric = event.matric, matricError = null)
                }
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
        val courseCodeResults = validateCourseCode.execute(roomFormState.value.courseCode)

        val hasError = listOf(
            courseCodeResults,
        ).any { !it.successful }

        roomFormState.update {
            it.copy(
                courseCodeError = courseCodeResults.errorMessage,
            )
        }

        if (hasError)
            return

        viewModelScope.launch {
            roomEventChannel.send(RoomEvent.Success)
        }
    }

    private fun submitRegistrationData() {
        val firstNameResult =
            validateFirstName.execute(updateProfileValidationFormState.value.firstName)
        val lastNameResult =
            validateLastName.execute(updateProfileValidationFormState.value.lastName)
        val emailResult = validateEmail.execute(updateProfileValidationFormState.value.email)
        val matricResult = validateMatric.execute(updateProfileValidationFormState.value.matric)


        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            matricResult
        ).any { !it.successful }

        updateProfileValidationFormState.update {
            it.copy(
                firstNameError = firstNameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                emailError = emailResult.errorMessage,
                matricError = matricResult.errorMessage
            )
        }

        if (hasError)
            return

        viewModelScope.launch {
            updateProfileValidationEventChannel.send(UpdateProfileValidationEvent.Success)
        }
    }

    fun updateUser() {
        val firstName = updateProfileValidationFormState.value.firstName
        val lastName = updateProfileValidationFormState.value.lastName
        val matric = updateProfileValidationFormState.value.matric
        val email = updateProfileValidationFormState.value.email

        val userHashMap = hashMapOf(
            "FirstName" to firstName,
            "LastName" to lastName,
            "Matric" to matric,
            "Email" to email
        )

        viewModelScope.launch {
            updateProfileEventChannel.send(UpdateProfileEvent.Loading)
        }
        repository.getAuthReference().currentUser?.updateEmail(email)
            ?.addOnCompleteListener { updateEmailTask ->
                if (updateEmailTask.isSuccessful) {
                    saveUserDetailsFirebase(email, userHashMap)
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
                    } else {
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

    private fun verifyIdentity() {

        val passwordResult = validateLoginPassword.execute(verifyIdentityFormState.value.password)


        val hasError = listOf(
            passwordResult
        ).any { !it.successful }

        verifyIdentityFormState.update {
            it.copy(
                passwordError = passwordResult.errorMessage
            )
        }

        if (hasError)
            return


        viewModelScope.launch {
            val email = repository.userEmail()
            verifyIdentityEventChannel.send(VerifyIdentityEvent.Loading)
            repository.getAuthReference()
                .signInWithEmailAndPassword(email, verifyIdentityFormState.value.password)
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