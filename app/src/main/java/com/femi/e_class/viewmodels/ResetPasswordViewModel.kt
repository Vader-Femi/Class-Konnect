package com.femi.e_class.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.femi.e_class.domain.use_case.ValidateEmail
import com.femi.e_class.presentation.ResetPasswordFormEvent
import com.femi.e_class.presentation.ResetPasswordFormState
import com.femi.e_class.repositories.ResetPasswordRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val repository: ResetPasswordRepository,
    private val validateEmail: ValidateEmail = ValidateEmail(),
) : BaseViewModel(repository) {

    var resetPasswordFormState by mutableStateOf(ResetPasswordFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val resetPasswordEventChannel = Channel<ResetPasswordEvent<Any?>>()
    val resetPasswordEvents = resetPasswordEventChannel.receiveAsFlow()

    fun onEvent(event: ResetPasswordFormEvent) {
        when (event) {
            is ResetPasswordFormEvent.EmailChanged -> {
                resetPasswordFormState = resetPasswordFormState.copy(email = event.email)
            }
            is ResetPasswordFormEvent.Submit -> {
                submitRegistrationData()
            }
        }
    }

    private fun submitRegistrationData() {
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
            validationEventChannel.send(ValidationEvent.Success)
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
                        resetPasswordEventChannel.send(ResetPasswordEvent.Error(sendResetPasswordTask.exception))
                    }
                }
            }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

    sealed class ResetPasswordEvent<out T> {
        object Success : ResetPasswordEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : ResetPasswordEvent<Nothing>()
        object Loading : ResetPasswordEvent<Nothing>()
    }
}