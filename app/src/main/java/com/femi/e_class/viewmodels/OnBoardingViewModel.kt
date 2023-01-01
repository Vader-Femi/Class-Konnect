package com.femi.e_class.viewmodels

import com.femi.e_class.data.repository.MainActivityRepositoryImpl
import com.femi.e_class.data.repository.OnBordingRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    repository: OnBordingRepositoryImpl
) : BaseViewModel(repository) {


}