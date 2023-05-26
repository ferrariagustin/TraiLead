package com.aferrari.trailead.app.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aferrari.trailead.domain.repository.UserRepository

class LoginViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}