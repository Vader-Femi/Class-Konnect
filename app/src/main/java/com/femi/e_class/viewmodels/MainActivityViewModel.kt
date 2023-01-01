package com.femi.e_class.viewmodels

import androidx.lifecycle.viewModelScope
import com.femi.e_class.data.repository.MainActivityRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    repository: MainActivityRepositoryImpl
) : BaseViewModel(repository) {


    private val _isLoading = MutableStateFlow(true)
    val isLoading: Boolean
        get() = _isLoading.value

    init {
        viewModelScope.launch {
            delay(2000L)
            _isLoading.value = false
        }
    }

}