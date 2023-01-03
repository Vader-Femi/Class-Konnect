package com.femi.e_class.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.femi.e_class.data.repository.base.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val repository: BaseRepositoryImpl,
) : ViewModel() {

    val useDynamicTheme = false

    private fun userEmail(email: String) = viewModelScope.launch {
        repository.userEmail(email)
    }

    suspend fun userEmail(): String = repository.userEmail()

    private fun userMatric(matric: Long) = viewModelScope.launch {
        repository.userMatric(matric)
    }

    suspend fun userMatric(): Long = repository.userMatric()

    private fun userFName(fName: String) = viewModelScope.launch {
        repository.userFName(fName)
    }

    suspend fun userFName(): String = repository.userFName()

    private fun userLName(lName: String) = viewModelScope.launch {
        repository.userLName(lName)
    }

    suspend fun userLName(): String = repository.userLName()

    fun videoResolution(resolution: Int) = viewModelScope.launch {
        repository.videoResolution(resolution)
    }

    suspend fun videoResolution(): Int = repository.videoResolution()

    open fun logOut(){
        viewModelScope.launch {
            userFName("")
            userLName("")
            userMatric(0L)
            userEmail("")
        }
    }

    open suspend fun isUserNew(): Boolean {
        val currentUser = repository.getAuthReference().currentUser
        val email = repository.userEmail()

        val isReturningUser = currentUser != null &&
                email.isNotEmpty() &&
                currentUser.email == email &&
                repository.userFName().isNotEmpty() &&
                repository.userLName().isNotEmpty() &&
                repository.userMatric() != 0L


        return !isReturningUser
    }
}
