package com.femi.e_class.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.femi.e_class.domain.use_case.ValidateEmail
import com.femi.e_class.domain.use_case.ValidatePassword
import com.femi.e_class.presentation.LogInFormEvent
import com.femi.e_class.presentation.LogInFormState
import com.femi.e_class.repositories.LogInRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LogInViewModel(
    private val repository: LogInRepository,
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
) : BaseViewModel(repository) {

    var logInFormState by mutableStateOf(LogInFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val logInEventChannel = Channel<LogInEvent<Any?>>()
    val logInEvents = logInEventChannel.receiveAsFlow()

    fun onEvent(event: LogInFormEvent) {
        when (event) {
            is LogInFormEvent.EmailChanged -> {
                logInFormState = logInFormState.copy(email = event.email)
            }
            is LogInFormEvent.PasswordChanged -> {
                logInFormState = logInFormState.copy(password = event.password)
            }
            is LogInFormEvent.Submit -> {
                submitRegistrationData()
            }
        }
    }

    private fun submitRegistrationData() {
        val emailResult = validateEmail.execute(logInFormState.email)
        val passwordResult = validatePassword.execute(logInFormState.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        logInFormState = logInFormState.copy(
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun logInUser(email: String, password: String) {
        viewModelScope.launch {
            logInEventChannel.send(LogInEvent.Loading)
        }
        repository.getAuthReference()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { addUserTask ->
                if (addUserTask.isSuccessful) {
                    viewModelScope.launch {
                        saveUser(email)
                    }
                } else {
                    viewModelScope.launch {
                        logInEventChannel.send(LogInEvent.Error(addUserTask.exception))
                    }
                }
            }
    }

    private fun saveUser(email: String) {
        repository.getCollectionReference()
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null)
                    viewModelScope.launch {
                        saveUserDetails(document.data)
                        logInEventChannel.send(LogInEvent.Success)
                    }
                else{
                    viewModelScope.launch {
                        logInEventChannel.send(LogInEvent.Empty("Incorrect email or password"))
                    }
                }
            }.addOnFailureListener { exception ->
                viewModelScope.launch {
                    logInEventChannel.send(LogInEvent.Error(exception))
                }
            }

    }

    private suspend fun saveUserDetails(data: MutableMap<String, Any>?) {
        repository.userFName(data?.get("FirstName")?.toString() ?: "")
        repository.userLName(data?.get("LastName")?.toString() ?: "")
        repository.userEmail(data?.get("Email")?.toString() ?: "")
        repository.userMatric(data?.get("Matric")?.toString()?.toLong() ?: 0L)
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

    sealed class LogInEvent<out T> {
        object Success : LogInEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : LogInEvent<Nothing>()
        data class Empty(val message: String) : LogInEvent<Nothing>()
        object Loading : LogInEvent<Nothing>()
    }
}