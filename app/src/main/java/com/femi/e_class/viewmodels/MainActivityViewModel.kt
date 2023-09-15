package com.femi.e_class.viewmodels

import androidx.lifecycle.viewModelScope
import com.femi.e_class.data.repository.main_activity.MainActivityRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class MainActivityViewModel @Inject constructor(
    repository: MainActivityRepositoryImpl,
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