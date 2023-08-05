package com.femi.e_class.presentation.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.femi.e_class.data.User
import com.femi.e_class.data.repository.authentication.AuthenticationRepositoryImpl
import com.femi.e_class.domain.use_case.*
import com.femi.e_class.presentation.authentication.LogIn.LogInFormEvent
import com.femi.e_class.presentation.authentication.LogIn.LogInFormState
import com.femi.e_class.presentation.authentication.ResetPassword.ResetPasswordFormEvent
import com.femi.e_class.presentation.authentication.ResetPassword.ResetPasswordFormState
import com.femi.e_class.presentation.authentication.SignUp.SignUpFormEvent
import com.femi.e_class.presentation.authentication.SignUp.SignUpFormState
import com.femi.e_class.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthenticationRepositoryImpl,
    private val validateFirstName: ValidateName,
    private val validateLastName: ValidateName,
    private val validateEmail: ValidateEmail,
    private val validateMatric: ValidateMatricOrId,
    private val validateSignUpPassword: ValidateSignUpPassword,
    private val validateLogInPassword: ValidateLogInPassword,
) : BaseViewModel(repository) {

    /*
    Sign Up
     */
    var signUpFormState by mutableStateOf(SignUpFormState())

    private val signUpFormEventChannel = Channel<FormEvent>()
    val signUpFormEvents = signUpFormEventChannel.receiveAsFlow()

    private val signUpEventChannel = Channel<SignUpEvent<Any?>>()
    val signUpEvents = signUpEventChannel.receiveAsFlow()

    fun onEvent(event: SignUpFormEvent) {
        when (event) {
            is SignUpFormEvent.FirstNameChanged -> {
                signUpFormState = signUpFormState.copy(firstName = event.firstName)
            }
            is SignUpFormEvent.LastNameChanged -> {
                signUpFormState = signUpFormState.copy(lastName = event.lastName)
            }
            is SignUpFormEvent.EmailChanged -> {
                signUpFormState = signUpFormState.copy(email = event.email)
            }
            is SignUpFormEvent.MatricChanged -> {
                signUpFormState = signUpFormState.copy(matric = event.matric)
            }
            is SignUpFormEvent.PasswordChanged -> {
                signUpFormState = signUpFormState.copy(password = event.password)
            }
            is SignUpFormEvent.Submit -> {
                submitSignUpData()
            }
        }
    }

    private fun submitSignUpData() {
        val firstNameResult = validateFirstName.execute(signUpFormState.firstName)
        val lastNameResult = validateLastName.execute(signUpFormState.lastName)
        val emailResult = validateEmail.execute(signUpFormState.email)
        val matricResult = validateMatric.execute(signUpFormState.matric)
        val passwordResult = validateSignUpPassword.execute(signUpFormState.password)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            matricResult,
            passwordResult
        ).any { !it.successful }

        signUpFormState = signUpFormState.copy(
            firstNameError = firstNameResult.errorMessage,
            lastNameError = lastNameResult.errorMessage,
            emailError = emailResult.errorMessage,
            matricError = matricResult.errorMessage,
            passwordError = passwordResult.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            signUpFormEventChannel.send(FormEvent.Success)
        }
    }

    fun signUserUp(user: User) {
        viewModelScope.launch {
            signUpEventChannel.send(SignUpEvent.Loading)
        }
        repository.getAuthReference()
            .createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { addUserTask ->
                if (addUserTask.isSuccessful) {
                    viewModelScope.launch {
                        storeUserInRemoteDB(
                            firstName = user.firstName,
                            lastName = user.lastName,
                            matric = user.matric,
                            email = user.email
                        )
                    }
                } else {
                    viewModelScope.launch {
                        signUpEventChannel.send(SignUpEvent.Error(addUserTask.exception))
                    }
                }
            }
    }

    private fun storeUserInRemoteDB(
        firstName: String,
        lastName: String,
        matric: String,
        email: String,
    ) {
        val userHashMap = hashMapOf(
            "FirstName" to firstName,
            "LastName" to lastName,
            "Matric" to matric,
            "Email" to email)

        repository.getCollectionReference()
            .document(email)
            .set(userHashMap)
            .addOnSuccessListener {
                viewModelScope.launch {
                    signUpEventChannel.send(SignUpEvent.Success)
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    signUpEventChannel.send(SignUpEvent.Error(it))
                }
            }
    }

    sealed class SignUpEvent<out T> {
        object Success : SignUpEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : SignUpEvent<Nothing>()
        object Loading : SignUpEvent<Nothing>()
    }
    

    
    /*
    Log In
     */
    var logInFormState by mutableStateOf(LogInFormState())

    private val logInFormEventChannel = Channel<FormEvent>()
    val logInFormEvents = logInFormEventChannel.receiveAsFlow()

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
                submitLogInData()
            }
        }
    }

    private fun submitLogInData() {
        val emailResult = validateEmail.execute(logInFormState.email)
        val passwordResult = validateLogInPassword.execute(logInFormState.password)

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
            logInFormEventChannel.send(FormEvent.Success)
        }
    }

    fun logUserIn(email: String, password: String) {
        viewModelScope.launch {
            logInEventChannel.send(LogInEvent.Loading)
        }
        repository.getAuthReference()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { addUserTask ->
                if (addUserTask.isSuccessful) {
                    viewModelScope.launch {
                        storeUserFromRemoteDB(email)
                    }
                } else {
                    viewModelScope.launch {
                        logInEventChannel.send(LogInEvent.Error(addUserTask.exception))
                    }
                }
            }
    }

    private fun storeUserFromRemoteDB(email: String) {
        repository.getCollectionReference()
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null)
                    viewModelScope.launch {
                        saveUserLocally(document.data)
                        logInEventChannel.send(LogInEvent.Success)
                    }
                else {
                    viewModelScope.launch {
                        logInEventChannel.send(LogInEvent.NoUser("Incorrect email or password"))
                    }
                }
            }.addOnFailureListener { exception ->
                viewModelScope.launch {
                    logInEventChannel.send(LogInEvent.Error(exception))
                }
            }

    }

    private suspend fun saveUserLocally(data: MutableMap<String, Any>?) {
        repository.userFName(data?.get("FirstName")?.toString() ?: "")
        repository.userLName(data?.get("LastName")?.toString() ?: "")
        repository.userEmail(data?.get("Email")?.toString() ?: "")
        repository.userMatric(data?.get("Matric")?.toString()?.toLong() ?: 0L)
    }
    
    sealed class LogInEvent<out T> {
        object Success : LogInEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : LogInEvent<Nothing>()
        data class NoUser(val message: String) : LogInEvent<Nothing>()
        object Loading : LogInEvent<Nothing>()
    }
    
    

    /*
    Reset Password
    */
    var resetPasswordFormState by mutableStateOf(ResetPasswordFormState())

    private val resetPasswordFormEventChannel = Channel<FormEvent>()
    val resetPasswordFormEvents = resetPasswordFormEventChannel.receiveAsFlow()

    private val resetPasswordEventChannel = Channel<ResetPasswordEvent<Any?>>()
    val resetPasswordEvents = resetPasswordEventChannel.receiveAsFlow()

    fun onEvent(event: ResetPasswordFormEvent) {
        when (event) {
            is ResetPasswordFormEvent.EmailChanged -> {
                resetPasswordFormState = resetPasswordFormState.copy(email = event.email)
            }
            is ResetPasswordFormEvent.Submit -> {
                submitResetPasswordData()
            }
        }
    }

    private fun submitResetPasswordData() {
        val emailResult = validateEmail.execute(resetPasswordFormState.email)

        val hasError = listOf(
            emailResult
        ).any { !it.successful }

        resetPasswordFormState = resetPasswordFormState.copy(
            emailError = emailResult.errorMessage
        )

        if (hasError)
            return

        viewModelScope.launch {
            resetPasswordFormEventChannel.send(FormEvent.Success)
        }
    }

    fun sendResetPassword(email: String) {
        viewModelScope.launch {
            resetPasswordEventChannel.send(ResetPasswordEvent.Loading)
        }
        repository.getAuthReference()
            .sendPasswordResetEmail(email)
            .addOnCompleteListener { sendResetPasswordTask ->
                if (sendResetPasswordTask.isSuccessful) {
                    viewModelScope.launch {
                        resetPasswordEventChannel.send(ResetPasswordEvent.Success)
                    }
                } else {
                    viewModelScope.launch {
                        resetPasswordEventChannel.send(
                            ResetPasswordEvent.Error(
                                sendResetPasswordTask.exception
                            )
                        )
                    }
                }
            }
    }

    sealed class ResetPasswordEvent<out T> {
        object Success : ResetPasswordEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : ResetPasswordEvent<Nothing>()
        object Loading : ResetPasswordEvent<Nothing>()
    }



    /*
    Common
     */
    sealed class FormEvent {
        object Success : FormEvent()
    }
}