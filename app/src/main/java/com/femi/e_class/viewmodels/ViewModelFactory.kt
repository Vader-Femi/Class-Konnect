package com.femi.e_class.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.femi.e_class.repositories.*

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> MainActivityViewModel(repository as MainActivityRepository) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(repository as SignUpRepository) as T
            modelClass.isAssignableFrom(LogInViewModel::class.java) -> LogInViewModel(repository as LogInRepository) as T
            modelClass.isAssignableFrom(HomeActivityViewModel::class.java) -> HomeActivityViewModel(repository as HomeActivityRepository) as T
            modelClass.isAssignableFrom(VideoActivityViewModel::class.java) -> VideoActivityViewModel(repository as VideoActivityRepository) as T
            modelClass.isAssignableFrom(ResetPasswordViewModel::class.java) -> ResetPasswordViewModel(repository as ResetPasswordRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}