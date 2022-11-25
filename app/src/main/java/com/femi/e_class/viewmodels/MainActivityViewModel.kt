package com.femi.e_class.viewmodels

import androidx.lifecycle.viewModelScope
import com.femi.e_class.repositories.MainActivityRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val repository: MainActivityRepository
) : BaseViewModel(repository) {


    private val _isLoading = MutableStateFlow(true)
    val isLoading: Boolean
        get() = _isLoading.value

    init {
        viewModelScope.launch {
            delay(1500)
            _isLoading.value = false
        }
    }
}