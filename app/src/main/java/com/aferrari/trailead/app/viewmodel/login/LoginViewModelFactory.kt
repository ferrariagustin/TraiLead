package com.aferrari.trailead.app.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository

class LoginViewModelFactory(
    private val remoteDataSource: RemoteDataSource
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(UserRepository(remoteDataSource)) as T
        }
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(UserRepository(remoteDataSource)) as T
        }
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(
                UserRepository(remoteDataSource),
                MaterialRepository(remoteDataSource)
            ) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}