package com.femi.e_class.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.femi.e_class.domain.use_case.*
import com.femi.e_class.presentation.RegistrationFormEvent
import com.femi.e_class.presentation.RegistrationFormState
import com.femi.e_class.presentation.SetPasswordFormEvent
import com.femi.e_class.presentation.SetPasswordFormState
import com.femi.e_class.repositories.SignUpRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: SignUpRepository,
    private val validateFirstName: ValidateName = ValidateName(),
    private val validateLastName: ValidateName = ValidateName(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validateMatric: ValidateMatric = ValidateMatric(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
) : BaseViewModel() {

    var registrationFormState by mutableStateOf(RegistrationFormState())
    var setPasswordFormState by mutableStateOf(SetPasswordFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val registrationEventChannel = Channel<RegistrationEvent>()
    val registrationEvents = registrationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.FirstNameChanged -> {
                registrationFormState = registrationFormState.copy(firstName = event.firstName)
            }
            is RegistrationFormEvent.LastNameChanged -> {
                registrationFormState = registrationFormState.copy(lastName = event.lastName)
            }
            is RegistrationFormEvent.EmailChanged -> {
                registrationFormState = registrationFormState.copy(email = event.email)
            }
            is RegistrationFormEvent.MatricChanged -> {
                registrationFormState = registrationFormState.copy(matric = event.matric)
            }
            is RegistrationFormEvent.Submit -> {
                submitRegistrationData()
            }
        }
    }

    private fun submitRegistrationData() {
        val firstNameResult = validateFirstName.execute(registrationFormState.firstName)
        val lastNameResult = validateLastName.execute(registrationFormState.lastName)
        val emailResult = validateEmail.execute(registrationFormState.email)
        val matricResult = validateMatric.execute(registrationFormState.matric)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            matricResult
        ).any { !it.successful }

        registrationFormState = registrationFormState.copy(
            firstNameError = firstNameResult.errorMessage,
            lastNameError = lastNameResult.errorMessage,
            emailError = emailResult.errorMessage,
            matricError = matricResult.errorMessage,
        )

        if (hasError)
            return

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }


    fun onEvent(event: SetPasswordFormEvent) {
        when (event) {
            is SetPasswordFormEvent.PasswordChanged -> {
                setPasswordFormState = setPasswordFormState.copy(password = event.password)
            }
            is SetPasswordFormEvent.RepeatedPasswordChanged -> {
                setPasswordFormState =
                    setPasswordFormState.copy(repeatedPassword = event.repeatedPassword)
            }
            is SetPasswordFormEvent.Submit -> {
                submitSignUpData()
            }
        }
    }

    private fun submitSignUpData() {
        val passwordResult = validatePassword.execute(setPasswordFormState.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            setPasswordFormState.password,
            setPasswordFormState.repeatedPassword)

        val hasError = listOf(
            passwordResult,
            repeatedPasswordResult
        ).any { !it.successful }

        setPasswordFormState = setPasswordFormState.copy(
            passwordError = passwordResult.errorMessage,
            repeatedPasswordError = repeatedPasswordResult.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun saveUser(
        firstName: String, lastName: String,
        matric: String, email: String,
    ) {
        val userHashMap = hashMapOf<String, String>()
        userHashMap["FirstName"] = firstName
        userHashMap["LastName"] = lastName
        userHashMap["Matric"] = matric
        userHashMap["Email"] = email

        repository.getCollectionReference()
            .add(userHashMap)
            .addOnSuccessListener {
                viewModelScope.launch {
                    registrationEventChannel.send(RegistrationEvent.Success)
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    registrationEventChannel.send(RegistrationEvent.Failed)
                }
            }
    }

    fun signUpUser(email: String, password: String) {

        repository.getAuthReference()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { addUserTask ->
                if (addUserTask.isSuccessful) {
                    viewModelScope.launch {
                        registrationEventChannel.send(RegistrationEvent.Success)
                    }
                } else {
                    viewModelScope.launch {
                        registrationEventChannel.send(RegistrationEvent.Failed)
                    }
                }
            }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

    sealed class RegistrationEvent {
        object Success : RegistrationEvent()
        object Failed : RegistrationEvent()
    }
}