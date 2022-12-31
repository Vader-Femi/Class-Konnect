package com.femi.e_class.viewmodels

import com.femi.e_class.data.repository.MainActivityRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    repository: MainActivityRepositoryImpl
) : BaseViewModel(repository) {

    /*
    private val _isLoading = MutableStateFlow(true)
    val isLoading: Boolean
        get() = _isLoading.value

    init {
        viewModelScope.launch {
            delay(10000L)
            _isLoading.value = false
        }
    }
     */
}